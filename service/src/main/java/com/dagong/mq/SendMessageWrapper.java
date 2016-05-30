package com.dagong.mq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/4/15.
 */
public class SendMessageWrapper {

    private String topic;
    @Resource
    private SendMessageService sendMessageService;

    public void sendMessage(String tag, Object message) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        sendMessageService.sendMessage(topic, tag, message);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
