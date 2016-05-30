package com.dagong.user.service;

//import com.alibaba.dubbo.config.annotation.Service;

import com.alibaba.dubbo.config.annotation.Service;
import com.dagong.mapper.*;
import com.dagong.pojo.*;
import com.dagong.user.UserClient;
import com.dagong.user.vo.UserVO;
import com.dagong.user.vo.WechatUserVO;
import com.dagong.util.BeanUtil;
import com.dagong.util.IdGenerator;
import com.google.common.base.Joiner;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuchang on 16/4/25.
 */
@Service(version = "1.0.0")
public class UserClientImpl implements UserClient {

    @Resource
    private UserMapper userMapper;
    @Resource
    private WechatUserMapper wechatUserMapper;

    @Resource
    private WantJobMapper wantJobMapper;
    @Resource
    private WantEnvironmentMapper wantEnvironmentMapper;
    @Resource
    private WantInformationMapper wantInformationMapper;

    @Resource
    private IdGenerator idGenerator;

    private static int USER_TYPE_WECHAT = 1;

    private static Jedis jedisClient = null;


    public UserClientImpl() {
        jedisClient = new Jedis("172.16.54.144", 6379);
        jedisClient.select(2);
    }

    //@Transactional
    public String createWechatUser(WechatUserVO wechatUserVO) {
        if (wechatUserVO == null) {
            return null;
        }
        User user = new User();
        WechatUser wechatUser = BeanUtil.getVO(wechatUserVO, WechatUser.class);
        String userId = idGenerator.generate(User.class.getSimpleName());
        user.setId(userId);
        user.setOpenId(wechatUserVO.getOpenid());
        user.setUserType(USER_TYPE_WECHAT);
        wechatUserMapper.insert(wechatUser);
        userMapper.insert(user);
        return userId;

    }


    public UserVO getUserByOpenId(String openId) {
        User user = userMapper.selectByOpenId(openId);
        if (user == null) {
            return null;
        }
        return BeanUtil.getVO(user,UserVO.class);
    }

    public boolean cacheUser(String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return false;
        }
        try {
            Map<String, String> stringMap = BeanUtils.describe(user);
            if (stringMap == null || stringMap.isEmpty()) {
                return false;
            }
            List emptyList = new ArrayList<>();
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                String value = entry.getValue();
                if (value == null || org.apache.commons.lang.StringUtils.isEmpty(value)) {
                    emptyList.add(entry.getKey());
                }
            }
            emptyList.forEach(value -> {
                stringMap.remove(value);
            });
            jedisClient.hmset(userId, stringMap);
            List<WantJob> wantJobs = wantJobMapper.selectByUserId(userId);
            List<WantEnvironment> wantEnvironments = wantEnvironmentMapper.selectByUserId(userId);
            List<WantInformation> wantInformations = wantInformationMapper.selectByUserId(userId);
            Map<String, String> infoMap = new HashMap();
            StringBuffer str = new StringBuffer();
            if (wantJobs != null && !wantJobs.isEmpty()) {
                for (WantJob wantJob : wantJobs) {
                    str.append(wantJob.getJobType()).append(",");
                }
                infoMap.put("wantJob", str.toString());
            }
            str = new StringBuffer();
            if (wantEnvironments != null && !wantEnvironments.isEmpty()) {
                for (WantEnvironment wantEnvironment : wantEnvironments) {
                    str.append(wantEnvironment.getEnvIds()).append(",");
                }
                infoMap.put("wantEnvironment", str.toString());
            }
            str = new StringBuffer();

            if (wantInformations != null && !wantInformations.isEmpty()) {
                for (WantInformation wantInformation : wantInformations) {
                    str.append(wantInformation.getiKey()).append(",");
                }
                infoMap.put("wantInformation", str.toString());
            }

            for (Map.Entry<String, String> entry : infoMap.entrySet()) {
                String value = entry.getValue();
                if (value == null || org.apache.commons.lang.StringUtils.isEmpty(value)) {
                    emptyList.add(entry.getKey());
                }
            }
            emptyList.forEach(value -> {
                infoMap.remove(value);
            });


            if (!infoMap.isEmpty()) {
                jedisClient.hmset(userId + "_info", infoMap);
            }
            return true;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }


    public UserVO getUserByUserId(String userId) {
        Map map = jedisClient.hgetAll(userId);
        if (map == null || map.isEmpty()) {
            if (cacheUser(userId)) {
                map = jedisClient.hgetAll(userId);
            }
        }

        if (map == null || map.isEmpty()) {
            return null;
        }

        UserVO userVO = new UserVO();
        try {
            BeanUtils.populate(userVO, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return userVO;


//
    }


    public Map<String, Object> getUserInfo(String userId) {
        Map map = jedisClient.hgetAll(userId + "_info");
        if (map == null || map.isEmpty()) {
            if (cacheUser(userId)) {
                map = jedisClient.hgetAll(userId + "_info");
            }
        }

        return map;
    }


}
