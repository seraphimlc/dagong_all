package com.dagong.service;

import com.alibaba.fastjson.JSON;
import com.dagong.pojo.Company;
import com.dagong.pojo.Evaluation;
import com.dagong.pojo.Job;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuchang on 16/4/5.
 */
@Service
public class SearchService {

    private TransportClient transportClient = null;
    private static final int PAGE_SIZE=10;
    private final String EVALUATION_INDEX = "evaluation";
    private final String JOB_EVALUATION_TYPE = "job";
    private final String USER_EVALUATION_TYPE = "user";
    private final String COMPANY_EVALUATION_TYPE = "company";

    private final String INDEX = "test";
    private final String TYPE = "company";



    public SearchService(){
        try {
            transportClient = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.16.54.144"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public List<Map> searchJobByType(String[] jobTypes,int page){
        List<Map> jobs = new ArrayList<>();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for(String jobType:jobTypes){
            queryBuilder.should(QueryBuilders.matchQuery("jobType",jobType));
        }
        SearchResponse searchResponse = transportClient.prepareSearch("test")
                .setTypes("job")
                .setQuery(queryBuilder)
                .setFrom(page)
                .setSize(PAGE_SIZE)
                .execute()
                .actionGet();
        System.out.println("searchResponse = " + searchResponse);
        for (SearchHit searchHitFields : searchResponse.getHits().getHits()) {
            jobs.add(searchHitFields.getSource());
        }
        return jobs;
    }


    public Map getJob(String jobId){
        GetResponse response = transportClient.prepareGet("test", "job", jobId).execute().actionGet();
        if(response.isExists()){
            return response.getSource();
        }
        return null;
    }

    public boolean addUserFollowJobToIndex(){
        return true;
    }

    public boolean addJobToIndex(List<Job> jobList){
        if(jobList==null||jobList.isEmpty()){
            return false;
        }
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        jobList.forEach(job -> {

            bulkRequestBuilder.add(transportClient.prepareIndex("test", "job", job.getId()).setSource(JSON.toJSONString(job)));
        });
        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();

        return true;
    }

    public boolean updateJobInIndex(List<Job> jobList){
        if(jobList==null||jobList.isEmpty()){
            return false;
        }
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        jobList.forEach(job -> {
            bulkRequestBuilder.add(transportClient.prepareUpdate("test", "job", job.getId()).setDoc(JSON.toJSONString(job)));
        });
        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();

        return true;
    }



    public List<Map> searchJobEvaluation(String jobId, int page,int pageSize) {
        return search(JOB_EVALUATION_TYPE, "jobId", jobId, page,pageSize);
    }

    public List<Map> searchUserEvaluation(String userId, int page,int pageSize) {
        return search(USER_EVALUATION_TYPE, "userId", userId, page,pageSize);
    }

    public List<Map> searchCompanyEvaluation(String companyId, int page,int pageSize) {
        return search(COMPANY_EVALUATION_TYPE, "companyId", companyId, page,pageSize);
    }

    public Map getJobEvaluation(String id) {
        return searchEvaluation(JOB_EVALUATION_TYPE, id);
    }

    public Map getCompnayEvaluation(String id) {
        return searchEvaluation(COMPANY_EVALUATION_TYPE, id);
    }

    public Map getUserEvaluation(String id) {
        return searchEvaluation(USER_EVALUATION_TYPE, id);
    }

    private Map searchEvaluation(String type, String id) {
        GetResponse response = transportClient.prepareGet(EVALUATION_INDEX, type, id).execute().actionGet();
        if (response.isExists()) {
            return response.getSource();
        }
        return null;
    }


    public boolean createEvaluation(List<Evaluation> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        list.forEach(evaluation -> {

            bulkRequestBuilder.add(transportClient.prepareIndex(EVALUATION_INDEX, evaluation.getType(), evaluation.getId()).setSource(JSON.toJSONString(evaluation)));
        });
        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();
        return true;
    }


    public boolean updateEvaluation(List<Evaluation> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        list.forEach(evaluation -> {
            bulkRequestBuilder.add(transportClient.prepareUpdate(EVALUATION_INDEX, evaluation.getType(), evaluation.getId()).setDoc(JSON.toJSONString(evaluation)));
        });
        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();

        return true;
    }


    private List<Map> search(String type, String key, String value, int page,int pageSize) {
        if(pageSize>PAGE_SIZE||pageSize<0){
            pageSize = PAGE_SIZE;
        }
        List<Map> evaluations = new ArrayList<>();

        SearchResponse searchResponse = transportClient.prepareSearch(EVALUATION_INDEX)
                .setTypes(type)
                .setQuery(QueryBuilders.matchQuery(key, value))
                .setFrom(page)
                .setSize(pageSize)
                .execute()
                .actionGet();
        for (SearchHit searchHitFields : searchResponse.getHits().getHits()) {
            evaluations.add(searchHitFields.getSource());
        }
        return evaluations;
    }


//
//
//    public Map getJob(String jobId){
//        GetResponse response = transportClient.prepareGet("test", "job", jobId).execute().actionGet();
//        if(response.isExists()){
//            return response.getSource();
//        }
//        return null;
//    }
//
//    public boolean addUserFollowJobToIndex(){
//        return true;
//    }
//
//    public boolean addJobToIndex(List<Job> jobList){
//        if(jobList==null||jobList.isEmpty()){
//            return false;
//        }
//        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
//        jobList.forEach(job -> {
//
//            bulkRequestBuilder.add(transportClient.prepareIndex("test", "job", job.getId()).setSource(JSON.toJSONString(job)));
//        });
//        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();
//
//        return true;
//    }
//
//    public boolean updateJobInIndex(List<Job> jobList){
//        if(jobList==null||jobList.isEmpty()){
//            return false;
//        }
//        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
//        jobList.forEach(job -> {
//            bulkRequestBuilder.add(transportClient.prepareUpdate("test", "job", job.getId()).setDoc(JSON.toJSONString(job)));
//        });
//        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();
//
//        return true;
//    }


    public Map getCompany(String id) {
        GetResponse response = transportClient.prepareGet(INDEX, TYPE, id).execute().actionGet();
        if (response.isExists()) {
            return response.getSource();
        }
        return null;
    }


    public boolean create(List<Company> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        list.forEach(company -> {

            bulkRequestBuilder.add(transportClient.prepareIndex(INDEX, TYPE, company.getId()).setSource(JSON.toJSONString(company)));
        });
        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();
        return true;
    }


    public boolean update(List<Company> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        list.forEach(company -> {
            bulkRequestBuilder.add(transportClient.prepareUpdate(INDEX, TYPE, company.getId()).setDoc(JSON.toJSONString(company)));
        });
        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();

        return true;
    }


}
