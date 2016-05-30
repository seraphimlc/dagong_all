package com.dagong.mapper;

import com.dagong.pojo.Company;

import java.util.List;

public interface CompanyMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(Company record);

    Company selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Company record);

    int updateByPrimaryKey(Company record);

    List<Company> selectByUserId(String userId);

    List<Company> selectByCompanyName(String companyName);


}