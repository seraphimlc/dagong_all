package com.dagong.service;

import com.dagong.util.ServiceConfiguration;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by liuchang on 16/5/29.
 */

@Service
public class RedisService {
    @Resource(name = "redisConfiguration")
    private ServiceConfiguration serviceConfiguration;

    private JedisPool jedisPool;
    private boolean isWorking = false;

    @PostConstruct
    public void init(){
        if (serviceConfiguration==null){
            return;
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(jedisPoolConfig,serviceConfiguration.getIp(),serviceConfiguration.getPort());

    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public Jedis getJedis(){
        return jedisPool.getResource();
    }

}
