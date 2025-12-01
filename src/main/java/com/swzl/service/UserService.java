package com.swzl.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swzl.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author swimt
* @description 针对表【user】的数据库操作Service
* @createDate 2024-11-18 21:12:05
*/
public interface UserService extends IService<User> {

    IPage pageC(IPage<User> page);

    IPage pageCC(IPage<User> page, Wrapper<User> Wrapper);
}
