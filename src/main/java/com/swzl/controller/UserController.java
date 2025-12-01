package com.swzl.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swzl.common.QueryPageParam;
import com.swzl.common.Result;
import com.swzl.entity.User;
import com.swzl.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public List<User> list() {
        return userService.list();
    }
    //增加
//    @PostMapping("/save")
//    public boolean save(@RequestBody User user) {
//        return userService.save(user);
//    }

    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no) {
      List list =  userService.lambdaQuery().eq(User::getNo,no).list();
      return list.size()>0?Result.suc(list):Result.fail("记录不存在");
    }

    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        return userService.save(user)?Result.suc() : Result.fail("记录不存在");
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return userService.updateById(user)?Result.suc() : Result.fail("记录不存在");
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        return userService.removeById(id)?Result.suc() : Result.fail("记录不存在");
    }

    //登入
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        List list =  userService.lambdaQuery()
                .eq(User::getNo,user.getNo())
                .eq(User::getPassword,user.getPassword()).list();
        return list.size()>0?Result.suc(list.get(0)) : Result.fail("记录不存在");
    }

    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user) {
        return userService.updateById(user);
    }
    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }
    //删除
    @GetMapping("/delete")
    public boolean delete(Integer id) {
        return userService.removeById(id);
    }
    //查询
    @PostMapping("/listP")
    public Result listP(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(user.getName())){
            lambdaQueryWrapper.like(User::getName,user.getName());
        }

        //lambdaQueryWrapper.like(User::getName,user.getName());//eq匹配
        return Result.suc(userService.list(lambdaQueryWrapper));
    }


   @PostMapping("/listPage")
    public List<User> listPage(@RequestBody QueryPageParam query) {
       System.out.println("num===" + query.getPageNum());
       System.out.println("size===" + query.getPageSize());

       HashMap param = query.getParam();
       String name = (String)param.get("name");
       System.out.println("name===" + (String)param.get("name"));
//       System.out.println("no===" + (String)param.get("no"));
//       Page<User> page = new Page<>(query.getPageNum(), query.getPageSize(),true);
       Page<User> page = new Page();
       page.setCurrent(query.getPageNum());
       page.setSize(query.getPageSize());

       LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
       lambdaQueryWrapper.like(User::getName,name);

       IPage<User> result = userService.page(page,lambdaQueryWrapper);
       System.out.println("total=="+result.getTotal());

//       System.out.println("Users on Current Page: " + result.getRecords().size());

       return result.getRecords();
    }

    @PostMapping("/listPageC")
    public List<User> listPageC(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        System.out.println("name===" + (String)param.get("name"));

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getName,name);


//        IPage result = userService.pageC(page);
        IPage result = userService.pageCC(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        System.out.println("name===" + (String)param.get("name"));

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(User::getName,name);
        }



//        IPage result = userService.pageC(page);
        IPage result = userService.pageCC(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return Result.suc(result.getRecords(),result.getTotal());
    }


}

