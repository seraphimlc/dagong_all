package com.dagong.mapper;

import com.dagong.pojo.JobEvaluation;

import java.util.List;

public interface JobEvaluationMapper {
    int deleteByPrimaryKey(String id);

    List<JobEvaluation> getList(JobEvaluation record);

    int insertSelective(JobEvaluation record);

    JobEvaluation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(JobEvaluation record);

}