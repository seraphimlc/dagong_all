package com.dagong.company;

import com.dagong.company.vo.CompanyVO;

/**
 * Created by liuchang on 16/5/27.
 */
public interface CompanyClient {
    CompanyVO getCompanyById(String companyId);
}
