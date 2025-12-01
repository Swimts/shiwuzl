package com.swzl.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swzl.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.yaml.snakeyaml.scanner.Constant;

/**
* @author swimt
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-11-18 21:12:05
* @Entity com.swzl.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

//    IPage pageC(Page<User> page);
    IPage<User> pageC(IPage<User> page);

    IPage<User> pageCC(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> Wrapper);

}




