package com.dagong.mapper;

import com.dagong.pojo.WantInformation;

import java.util.List;

public interface WantInformationMapper {
    int deleteByPrimaryKey(String id);

    int insert(WantInformation record);

    int insertSelective(WantInformation record);

    WantInformation selectByPrimaryKey(String id);

    List<WantInformation> selectByUserId(String userId);

    int updateByPrimaryKeySelective(WantInformation record);

    int updateByPrimaryKey(WantInformation record);
}