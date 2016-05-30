package com.dagong.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.dagong.evaluation.vo.CompanyEvaluationVO;
import com.dagong.evaluation.vo.JobEvaluationVO;
import com.dagong.evaluation.vo.UserEvaluationVO;
import com.dagong.evaluation.vo.VOList;
import com.dagong.job.JobClient;
import com.dagong.job.vo.JobVO;
import com.dagong.mapper.CompanyEvaluationMapper;
import com.dagong.mapper.JobEvaluationMapper;
import com.dagong.mapper.UserEvaluationMapper;
import com.dagong.mq.SendMessageService;
import com.dagong.pojo.CompanyEvaluation;
import com.dagong.pojo.Evaluation;
import com.dagong.pojo.JobEvaluation;
import com.dagong.pojo.UserEvaluation;
import com.dagong.user.UserClient;
import com.dagong.user.vo.UserVO;
import com.dagong.util.BeanUtil;
import com.dagong.util.IdGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuchang on 16/5/20.
 */
@Service
public class EvaluationService {
    @Resource
    private JobEvaluationMapper jobEvaluationMapper;

    @Resource
    private UserEvaluationMapper userEvaluationMapper;

    @Resource
    private CompanyEvaluationMapper companyEvaluationMapper;

    @Reference(version = "1.0.0")
    private UserClient userClient;

    @Reference(version = "1.0.0")
    private JobClient jobClient;

    @Resource
    private IdGenerator idGenerator;

    @Resource
    private SearchService searchService;

    @Resource
    private SendMessageService sendMessageService;

    private boolean switch_for_index_sync = false;

    public boolean evaluateCompany(String userId, String companyId, int rank, String comment) {

        CompanyEvaluation companyEvaluation = new CompanyEvaluation();
        companyEvaluation.setUserId(userId);
        companyEvaluation.setCompanyId(companyId);
        List<CompanyEvaluation> list = companyEvaluationMapper.getList(companyEvaluation);
        if (list != null && !list.isEmpty()) {
            return false;
        }
        companyEvaluation.setComment(comment);
        companyEvaluation.setRank(rank);
        companyEvaluation.setCreateTime(System.currentTimeMillis());
        companyEvaluation.setId(idGenerator.generate(CompanyEvaluation.class.getSimpleName()));
        companyEvaluation.setStatus(1);
        companyEvaluationMapper.insertSelective(companyEvaluation);
        addToIndex(companyEvaluation);
        return true;
    }


    public boolean evaluateUser(String companyUser, String companyId, String userId, int rank, String comment) {
        UserEvaluation userEvaluation = new UserEvaluation();
        userEvaluation.setCompanyUser(companyUser);
        userEvaluation.setCompanyId(companyId);
        userEvaluation.setUserId(userId);
        List<UserEvaluation> list = userEvaluationMapper.getList(userEvaluation);
        if (list != null && !list.isEmpty()) {
            return false;
        }
        UserVO userVO = userClient.getUserByUserId(userId);
        if (userVO == null) {
            return false;
        }
        userEvaluation.setId(idGenerator.generate(UserEvaluation.class.getSimpleName()));
        userEvaluation.setCreateTime(System.currentTimeMillis());
        userEvaluation.setRank(rank);
        userEvaluation.setStatus(1);
        userEvaluation.setComment(comment);
        userEvaluation.setInfo(userVO.toString());
        userEvaluationMapper.insertSelective(userEvaluation);
        addToIndex(userEvaluation);
        return true;
    }

    public boolean evaluateJob(String userId, String jobId, int rank, String comment) {

        JobEvaluation jobEvaluation = new JobEvaluation();
        jobEvaluation.setUserId(userId);
        jobEvaluation.setJobId(jobId);
        List<JobEvaluation> list = jobEvaluationMapper.getList(jobEvaluation);
        if (list != null && !list.isEmpty()) {
            return false;
        }
        JobVO job = jobClient.getJobByJobId(jobId);
        if (job == null) {
            return false;
        }
        jobEvaluation.setId(idGenerator.generate(JobEvaluation.class.getSimpleName()));
        jobEvaluation.setComment(comment);
        jobEvaluation.setStatus(1);
        jobEvaluation.setCreateTime(System.currentTimeMillis());
        jobEvaluation.setRank(rank);
        jobEvaluationMapper.insertSelective(jobEvaluation);
        addToIndex(jobEvaluation);
        return true;

    }

    public VOList<CompanyEvaluationVO> getCompanyEvaluationForUser(String userId, int page, int pageSize) {
        if (pageSize > 20 || pageSize <= 0) {
            pageSize = 20;
        }
        PageHelper.startPage(page, pageSize);
        CompanyEvaluation companyEvaluation = new CompanyEvaluation();
        companyEvaluation.setUserId(userId);
        return convertPagesToVOList((Page) companyEvaluationMapper.getList(companyEvaluation), CompanyEvaluationVO.class);

    }

    public VOList<JobEvaluationVO> getJobEvaluationForUser(String userId, int page, int pageSize) {
        if (pageSize > 20 || pageSize <= 0) {
            pageSize = 20;
        }
        PageHelper.startPage(page, pageSize);
        JobEvaluation jobEvaluation = new JobEvaluation();
        jobEvaluation.setUserId(userId);
        return convertPagesToVOList((Page) jobEvaluationMapper.getList(jobEvaluation), JobEvaluationVO.class);
    }

    public VOList<UserEvaluationVO> getUserEvaluationForCompany(String companyId, int page, int pageSize) {
        if (pageSize > 20 || pageSize <= 0) {
            pageSize = 20;
        }
        PageHelper.startPage(page, pageSize);
        UserEvaluation evaluation = new UserEvaluation();
        evaluation.setCompanyId(companyId);
        return convertPagesToVOList((Page) userEvaluationMapper.getList(evaluation), UserEvaluationVO.class);
    }




    public <T> VOList convertPagesToVOList(Page pages, Class<T> clazz) {
        if (pages != null && !pages.isEmpty()) {
            VOList voList = new VOList();
            voList.setEndRow(pages.getEndRow());
            voList.setPageNum(pages.getPageNum());
            voList.setPages(pages.getPages());
            voList.setPageSize(pages.getPageSize());
            voList.setTotal(pages.getTotal());
            voList.setStartRow(pages.getStartRow());
            pages.forEach(evaluation -> {
                voList.add(BeanUtil.getVO(evaluation, clazz));
            });
            return voList;
        }
        return null;
    }


    public CompanyEvaluation getCompnayEvaluationById(String id) {
        return companyEvaluationMapper.selectByPrimaryKey(id);
    }

    public JobEvaluation getJobEvaluationById(String id) {
        return jobEvaluationMapper.selectByPrimaryKey(id);
    }

    public UserEvaluation getUserEvaluationById(String id) {
        return userEvaluationMapper.selectByPrimaryKey(id);
    }

    public List<Map> getCompanyEvaluation(String companyId, int page, int pageSize) {
        return searchService.searchCompanyEvaluation(companyId, page, pageSize);
    }

    public List<Map> getUserEvaluation(String userId, int page, int pageSize) {
        return searchService.searchUserEvaluation(userId, page, pageSize);
    }

    public List<Map> getJobEvaluation(String jobId, int page, int pageSize) {
        return searchService.searchJobEvaluation(jobId, page, pageSize);
    }

    private void addToIndex(Evaluation evaluation) {
        if (switch_for_index_sync) {
            addEvaluationToIndex(Lists.newArrayList(evaluation));
        } else {
            try {
                sendMessage(evaluation);
            } catch (Exception e) {
                addEvaluationToIndex(Lists.newArrayList(evaluation));
            }
        }
    }

    public void addEvaluationToIndex(List<Evaluation> list) {
        searchService.createEvaluation(list);
    }


    private void sendMessage(Evaluation evaluation) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        if (evaluation == null) {
            return;
        }
        Map<String, String> map = new HashMap();
        map.put("type", evaluation.getType());
        map.put("id", evaluation.getId());
        sendMessageService.sendMessage("evaluation", "create", map);
    }
}
