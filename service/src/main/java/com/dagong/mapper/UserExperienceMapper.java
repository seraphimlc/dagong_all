package com.dagong.mapper;

import com.dagong.pojo.UserExperience;

public interface UserExperienceMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserExperience record);

    int insertSelective(UserExperience record);

    UserExperience selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserExperience record);

    int updateByPrimaryKey(UserExperience record);

    UserExperience selectByUserId(String userId);
}