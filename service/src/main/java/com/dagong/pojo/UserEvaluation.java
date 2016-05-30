package com.dagong.pojo;

public class UserEvaluation extends Evaluation{


    private String companyUser;
    private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyUser() {
        return companyUser;
    }

    public void setCompanyUser(String companyUser) {
        this.companyUser = companyUser;
    }

    @Override
    public String getType() {
        return "user";
    }
}