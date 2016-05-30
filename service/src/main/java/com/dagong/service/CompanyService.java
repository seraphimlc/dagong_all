package com.dagong.service;

import com.dagong.mapper.CompanyMapper;
import com.dagong.pojo.Company;
import com.dagong.util.IdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/1/17.
 */
@Service
public class CompanyService {

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private IdGenerator idGenerator;

    private static int COMPANY_STATUS_INIT = 1;



//    public int createCompany(String userId, String companyName,
//                             int companyType,
//                             int industry,
//                             int scale,
//                             String area,
//                             String address,
//                             String telephone, String contact) {
//        List<Company> list = companyMapper.selectByUserId(userId);
//        if (list != null && !list.isEmpty()) {
//            return 2;
//        }
//        list = companyMapper.selectByCompanyName(companyName);
//        if (list != null && !list.isEmpty()) {
//            return 3;
//        }
//
//        Company company = new Company();
//        company.setId(idGenerator.generate(Company.class.getSimpleName()));
//        company.setCompanytype(companyType);
//        company.setIndustry(industry);
//        company.setScale(scale);
//        company.setArea(area);
//        company.setAddress(address);
//        company.setTelephone(telephone);
//        company.setContact(contact);
//        company.setVersion(1);
//        company.setCreateTime(System.currentTimeMillis());
//        company.setStatus(COMPANY_STATUS_INIT);
//        company.setCreateUser(userId);
//        company.setModifyTime(System.currentTimeMillis());
//        companyMapper.insertSelective(company);
//        return 1;
//    }
//
//
//    public boolean modifyCompany(String companyId,String userId, String companyName,
//                             int companyType,
//                             int industry,
//                             int scale,
//                             String area,
//                             String address,
//                             String telephone, String contact,int version) {
//
//        Company company = new Company();
//        company.setId(companyId);
//        company.setCompanytype(companyType);
//        company.setIndustry(industry);
//        company.setScale(scale);
//        company.setArea(area);
//        company.setAddress(address);
//        company.setTelephone(telephone);
//        company.setContact(contact);
//        company.setVersion(version);
//        company.setCreateUser(userId);
//        company.setModifyTime(System.currentTimeMillis());
//        companyMapper.updateByPrimaryKeySelective(company);
//        return true;
//    }
//
//
//    public boolean addDescription(String userId,String companyId,String description,int version){
//
//        Company newCompany  = new Company();
//        newCompany.setId(companyId);
//        newCompany.setDescription(description);
//        newCompany.setModifyTime(System.currentTimeMillis());
//        newCompany.setVersion(version);
//        companyMapper.updateByPrimaryKeySelective(newCompany);
//        return true;
//    }

    public Company getCompanyById(String id){
        return companyMapper.selectByPrimaryKey(id);
    }


}
