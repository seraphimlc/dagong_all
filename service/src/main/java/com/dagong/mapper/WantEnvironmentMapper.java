package com.dagong.mapper;

import com.dagong.pojo.WantEnvironment;
import com.dagong.pojo.WantInformation;

import java.util.List;

public interface WantEnvironmentMapper {
    int deleteByPrimaryKey(String id);

    int insert(WantEnvironment record);

    int insertSelective(WantEnvironment record);

    WantEnvironment selectByPrimaryKey(String id);
    List<WantEnvironment> selectByUserId(String userId);

    int updateByPrimaryKeySelective(WantEnvironment record);

    int updateByPrimaryKey(WantEnvironment record);
}