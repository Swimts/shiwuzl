package com.swzl.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swzl.entity.Lostandfound;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author swimt
* @description 针对表【lostandfound】的数据库操作Service
* @createDate 2024-11-25 19:58:47
*/
public interface LostandfoundService extends IService<Lostandfound> {

    boolean save(Lostandfound lostandfound);


    IPage pageCC(IPage<Lostandfound> page, Wrapper<Lostandfound> Wrapper);

    boolean claimItem(Integer itemId, Integer currentUserId);
}
