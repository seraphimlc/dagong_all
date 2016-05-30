package com.dagong.mq.job;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.dagong.mq.MessageProcessor;
import com.dagong.pojo.Company;
import com.dagong.pojo.Job;
import com.dagong.service.CompanyService;
import com.dagong.service.JobService;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchang on 16/4/16.
 */

public class CreateJobMessageProcessor extends MessageProcessor {


    @Resource
    private JobService jobService;
    @Resource
    private CompanyService companyService;

    public CreateJobMessageProcessor() {
        this.setTopic("job");
        this.setTag("createJob");
    }

    @Override
    protected void process(List<MessageExt> list) {

        List<Job> jobList = new ArrayList<>();
        list.forEach(messageExt -> {
            System.out.println("messageExt = " + messageExt.getTags());
            try {
                String jobId = new String(messageExt.getBody(), "UTF-8");
                Job job = jobService.getJobFromDB(jobId);
                if (job == null) {
                    return;
                }
                String companyId = job.getCompanyId();
                Company company = companyService.getCompanyById(companyId);
                job.setCompanyName(company.getName());
                jobList.add(job);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        jobService.addJobToIndex(jobList);
    }
}
