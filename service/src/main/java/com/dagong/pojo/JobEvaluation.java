package com.dagong.pojo;

public class JobEvaluation extends Evaluation {
    private String jobId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String getType() {
        return "job";
    }
}