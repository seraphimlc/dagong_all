package com.dagong.pojo;

public class CompanyEvaluation extends Evaluation {

    private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public String getType() {
        return "company";
    }
}