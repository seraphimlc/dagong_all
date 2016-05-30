package com.dagong.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.dagong.user.TestUserClient;
import com.dagong.user.UserClient;
import com.dagong.user.vo.UserVO;
import com.dagong.user.vo.WechatUserVO;
import org.apache.ibatis.io.ResolverUtil;

import java.util.Map;

/**
 * Created by liuchang on 16/4/30.
 */
@Service(version = "1.0.0")
public class TestUserClientImpl implements TestUserClient {

    @Override
    public void test() {
        System.out.println("TestUserClientImpl.test");
    }
}
