package com.dagong.mq.company;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.dagong.mq.MessageProcessor;
import com.dagong.pojo.Company;
import com.dagong.service.CompanyService;
import com.dagong.service.SearchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchang on 16/4/16.
 */

@Service
public class CreateCompanyMessageProcessor extends MessageProcessor {


    @Resource
    private CompanyService companyService;

    @Resource
    private SearchService searchService;


    public CreateCompanyMessageProcessor() {
        this.setTopic("company");
        this.setTag("create");
    }

    @Override
    protected void process(List<MessageExt> list) {

        List<Company> companyList = new ArrayList<>();
        list.forEach(messageExt -> {
            System.out.println("messageExt = " + messageExt.getTags());
            try {
                String json = new String(messageExt.getBody(), "UTF-8");
                List<String> arrayList = JSON.parse(json, ArrayList.class);
                if (arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                arrayList.forEach(id->{
                     Company company = companyService.getCompanyById(id);
                    companyList.add(company);
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        if(!companyList.isEmpty()){
            searchService.create(companyList);
        }
    }
}
