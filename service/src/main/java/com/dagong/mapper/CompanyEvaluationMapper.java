package com.dagong.mapper;

import com.dagong.pojo.CompanyEvaluation;

import java.util.List;

public interface CompanyEvaluationMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(CompanyEvaluation record);

    CompanyEvaluation selectByPrimaryKey(String id);

    List<CompanyEvaluation> getList(CompanyEvaluation companyEvaluation);

    int updateByPrimaryKeySelective(CompanyEvaluation record);

}