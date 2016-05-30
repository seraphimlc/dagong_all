package com.dagong.company.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.dagong.company.CompanyClient;
import com.dagong.company.vo.CompanyVO;
import com.dagong.pojo.Company;
import com.dagong.service.CompanyService;
import com.dagong.util.BeanUtil;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/5/27.
 */
@Service(version = "1.0.0")
public class CompanyClientImpl implements CompanyClient {
    @Resource
    private CompanyService companyService;

    @Override
    public CompanyVO getCompanyById(String companyId) {
        Company company = companyService.getCompanyById(companyId);
        return BeanUtil.getVO(company, CompanyVO.class);
    }
}
