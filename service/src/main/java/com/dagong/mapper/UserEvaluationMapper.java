package com.dagong.mapper;

import com.dagong.pojo.UserEvaluation;

import java.util.List;

public interface UserEvaluationMapper {
    int deleteByPrimaryKey(String id);

    List<UserEvaluation> getList(UserEvaluation record);

    int insertSelective(UserEvaluation record);

    UserEvaluation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserEvaluation record);

}