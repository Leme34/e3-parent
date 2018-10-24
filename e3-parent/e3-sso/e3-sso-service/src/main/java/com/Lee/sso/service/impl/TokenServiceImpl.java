package com.Lee.sso.service.impl;

import com.Lee.dto.E3Result;
import com.Lee.jedis.JedisClient;
import com.Lee.pojo.TbUser;
import com.Lee.sso.service.TokenService;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 根据请求中的cookie中的token查询缓存取得该用户的用户信息
 * 若缓存中没有则认为登录状态已过期，返回状态码201
 * 有则是已登录状态，更新用户过期时间,返回缓存内容(用户信息)
 */
@Component
@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public E3Result getUserByToken(String token) {
        //查询缓存
        String userJson = jedisClient.get("SESSION:" + token);
        //若缓存中没有
        if (StringUtils.isEmpty(userJson)){
            return E3Result.build(201,"用户登录已过期");
        }
        //更新用户过期时间
        jedisClient.expire("SESSION:" + token,SESSION_EXPIRE);
        //返回反序列化后的用户对象
        Gson gson = new Gson();
        TbUser user = gson.fromJson(userJson, TbUser.class);
        return E3Result.ok(user);
    }
}
