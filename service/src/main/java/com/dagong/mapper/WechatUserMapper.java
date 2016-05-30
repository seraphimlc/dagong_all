package com.dagong.mapper;

import com.dagong.pojo.WechatUser;

public interface WechatUserMapper {
    int deleteByPrimaryKey(String openid);

    int insert(WechatUser record);

    int insertSelective(WechatUser record);

    WechatUser selectByPrimaryKey(String openid);

    int updateByPrimaryKeySelective(WechatUser record);

    int updateByPrimaryKey(WechatUser record);
}