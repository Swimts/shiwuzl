package com.swzl.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swzl.entity.Lostandfound;
import com.swzl.entity.User;
import com.swzl.service.LostandfoundService;
import com.swzl.mapper.LostandfoundMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author swimt
* @description 针对表【lostandfound】的数据库操作Service实现
* @createDate 2024-11-25 19:58:47
*/
@Service
public class LostandfoundServiceImpl extends ServiceImpl<LostandfoundMapper, Lostandfound>
    implements LostandfoundService{

    @Resource
    private LostandfoundMapper lostandfoundMapper;

    public LostandfoundServiceImpl(LostandfoundMapper lostandfoundMapper) {
        this.lostandfoundMapper = lostandfoundMapper;
    }

    @Override
    public boolean save(Lostandfound lostandfound) {
        return lostandfoundMapper.insert(lostandfound) > 0;
    }

    @Override
    public IPage pageCC(IPage<Lostandfound> page, Wrapper<Lostandfound> Wrapper) {
        return lostandfoundMapper.pageCC(page,Wrapper);
    }

    @Override
    public boolean claimItem(Integer itemId, Integer currentUserId) {
        return lostandfoundMapper.updateClaimedById(itemId, currentUserId) > 0;
    }

}




