package com.dagong.mapper;

import com.dagong.pojo.WantJob;

import java.util.List;

public interface WantJobMapper {
    int deleteByPrimaryKey(String id);

    int insert(WantJob record);

    int insertSelective(WantJob record);

    WantJob selectByPrimaryKey(String id);

    List<WantJob> selectByUserId(String userId);

    int updateByPrimaryKeySelective(WantJob record);

    int updateByPrimaryKey(WantJob record);
}