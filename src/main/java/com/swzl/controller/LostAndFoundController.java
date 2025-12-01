package com.swzl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swzl.common.QueryPageParam;
import com.swzl.common.Result;
import com.swzl.entity.Lostandfound;
import com.swzl.service.LostandfoundService;
import com.swzl.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/lostandfound")
public class LostAndFoundController<ClaimRequest> {
    private static final String IMAGE_STORAGE_DIR = "upload/images";
    @Autowired
    private LostandfoundService lostandfoundService;
    @Autowired
    private UserService userService;
    @Autowired
    private ServletContext servletContext;


    @GetMapping("/list")
    public List<Lostandfound> list() {
        return lostandfoundService.list();
    }
    //保存
    @PostMapping("/save")
    public Result save(@RequestBody Lostandfound lostandfound) {
        return lostandfoundService.save(lostandfound) ? Result.suc() : Result.fail("记录不存在");
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        // 根据id查询记录
        Lostandfound lostandfound = lostandfoundService.getById(id);
        if (lostandfound == null) {
            return Result.fail("记录不存在");
        }

        // 删除文件
        // 删除文件
        if (lostandfound.getImagepath() != null && !lostandfound.getImagepath().isEmpty()) {
            File file = new File("upload" + File.separator + "images" + File.separator + lostandfound.getImagepath());
            try {
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        return Result.fail("删除文件失败");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.fail("文件删除异常: " + e.getMessage());
            }
        }


        // 删除数据库记录
        boolean result = lostandfoundService.removeById(id);
        return result ? Result.suc() : Result.fail("删除失败");
    }

    //认领
    @PostMapping("/claim")
    public ResponseEntity<?> claimItem(@RequestParam("itemId") int itemId, @RequestBody Map<String, String> body) {
        try {
            Integer userId = Integer.parseInt(body.get("userId"));
            lostandfoundService.claimItem(itemId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody Lostandfound lostandfound) {
        return lostandfoundService.updateById(lostandfound);
    }

    //获取图片
    @GetMapping("/getImage/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") String imageId) {
        try {
            // 根据数据库中图片的存储格式，拼接出完整的图片路径
            String imagePath = IMAGE_STORAGE_DIR + "/" + imageId;

            // 获取文件对象
            File imageFile = new File(imagePath);

            // 获取文件扩展名，用于确定图片格式（这里其实已经知道是.jpg了，但为了代码通用性，还是保留获取扩展名的步骤）
            String fileExtension = getFileExtension(imagePath);

            // 根据图片格式设置Content-Type头部信息
            MediaType mediaType = getMediaTypeByExtension(fileExtension);

            // 创建输入流读取图片数据
            InputStream inputStream = new FileInputStream(imageFile);
            byte[] imageData = inputStream.readAllBytes();

            // 设置响应头部信息
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);

            // 返回包含图片数据的响应实体
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            // 处理获取图片数据过程中出现的错误
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String getFileExtension(String filePath) {
        int lastIndex = filePath.lastIndexOf('.');
        if (lastIndex > 0) {
            return filePath.substring(lastIndex + 1);
        }
        return "";
    }

    private MediaType getMediaTypeByExtension(String fileExtension) {
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            default:
                // 如果遇到不支持的格式，可以返回一个默认的格式或者抛出异常，这里返回默认的JPEG格式
                return MediaType.IMAGE_JPEG;
        }
    }



    //上传图片
    @PostMapping("/saveWithImage")
    public ResponseEntity<Result> saveWithImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("lostandfound") String lostandfoundJson) {

        System.out.println("Received image: " + image);
        try {
            // 将 JSON 字符串转为 Lostandfound 对象
            ObjectMapper objectMapper = new ObjectMapper();
            Lostandfound lostandfound = objectMapper.readValue(lostandfoundJson, Lostandfound.class);

            // 生成唯一的文件名，避免文件名冲突
            String originalFileName = image.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new IllegalArgumentException("File name is empty");
            }

            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // 获取上传目录路径
            String uploadDir = "upload/images";  // 可以考虑从配置文件中读取
            Path uploadPath = Paths.get(uploadDir);

            // 确保目录存在
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path savedFilePath = uploadPath.resolve(uniqueFileName);
            Files.write(savedFilePath, image.getBytes());

            // 设置失物招领信息中的图片路径
            lostandfound.setImagePath(uniqueFileName);

            // 保存失物招领信息到数据库
            boolean saved = lostandfoundService.save(lostandfound);
            if (saved) {
                return new ResponseEntity<>(Result.suc(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Result.fail("记录不存在"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Result.fails(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(Result.fails("File processing error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(Result.fails("Unexpected error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        Integer userid = (Integer) param.get("userid");  // 确保是 Integer 类型
        Integer roleId = (Integer) param.get("roleId");  // 确保是 Integer 类型
        Integer lostandfound = (Integer) param.get("lostorfound");  // 确保是 Integer 类型

        // 输出调试信息，检查传入的参数
        System.out.println("name===" + name);
        System.out.println("userid===" + userid);
        System.out.println("roleId===" + roleId);

        // 分页参数
        Page<Lostandfound> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        // 查询条件
        LambdaQueryWrapper<Lostandfound> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Lostandfound::getItemname, name);
        }

        // 根据角色判断是否限制查询结果
        if (roleId == null || (roleId != 0 && roleId != 1)) { // 普通用户只能看到自己的数据
            lambdaQueryWrapper.eq(Lostandfound::getUserid, userid);  // 只查询该用户发布的数据
        }

        lambdaQueryWrapper.eq(Lostandfound::getLostorfound, lostandfound);

        // 执行查询
        IPage<Lostandfound> result = lostandfoundService.pageCC(page, lambdaQueryWrapper);

        System.out.println("total==" + result.getTotal());
        System.out.println("result.getRecords()=" + result.getRecords());

        return Result.suc(result.getRecords(), result.getTotal());
    }




    //查询
    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        Integer lostandfound = (Integer) param.get("lostorfound");
        System.out.println("name===" + (String)param.get("name"));

        Page<Lostandfound> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Lostandfound> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Lostandfound::getItemname,name);
        }

        lambdaQueryWrapper.eq(Lostandfound::getLostorfound,lostandfound);

        IPage result = lostandfoundService.pageCC(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return Result.suc(result.getRecords(),result.getTotal());
    }
}
