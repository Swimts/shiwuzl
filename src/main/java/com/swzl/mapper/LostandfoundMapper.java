package com.swzl.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.swzl.entity.Lostandfound;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author swimt
* @description 针对表【lostandfound】的数据库操作Mapper
* @createDate 2024-11-25 19:58:47
* @Entity com.swzl.entity.Lostandfound
*/

@Mapper
public interface LostandfoundMapper extends BaseMapper<Lostandfound> {

    IPage pageCC(IPage<Lostandfound> page, @Param(Constants.WRAPPER) Wrapper<Lostandfound> wrapper);

    int updateClaimedById(@Param("id") Integer id, @Param("claimedUserId") Integer claimedUserId);
}




