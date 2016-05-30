package com.dagong.user;

import com.dagong.user.vo.UserVO;
import com.dagong.user.vo.WechatUserVO;

import java.util.Map;

/**
 * Created by liuchang on 16/4/25.
 */
public interface UserClient {
    String createWechatUser(WechatUserVO wechatUserVO);
    UserVO getUserByUserId(String userId);
    UserVO getUserByOpenId(String openId);
    Map<String,Object> getUserInfo(String userId);
}
