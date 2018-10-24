package com.Lee.sso.service.impl;

import com.Lee.dao.TbUserMapper;
import com.Lee.dto.E3Result;
import com.Lee.jedis.JedisClient;
import com.Lee.pojo.TbUser;
import com.Lee.pojo.TbUserExample;
import com.Lee.sso.service.LoginService;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Component
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbUserMapper tbUserMapper;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    /**
     * 参数：用户名和密码
     * 业务逻辑：
     * 1、判断用户和密码是否正确
     * 2、如果不正确，返回登录失败
     * 3、如果正确生成token。
     * 4、把用户信息写入redis，key：token value：用户信息
     * 5、设置Session的过期时间
     * 6、把token返回
     * 返回值：E3Result，其中包含token信息
     */
    @Override
    public E3Result userLogin(String username, String password) {
        //查询用户信息
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list == null && list.size() == 0) {
            return E3Result.build(400, "用户名或密码错误");
        }
        //取用户信息
        TbUser user = list.get(0);
        //密码不正确
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            return E3Result.build(400, "用户名或密码错误");
        }

        //密码正确生成token
        String token = UUID.randomUUID().toString();
        //把用户信息写入redis，key：token value：用户信息
        Gson gson = new Gson();
        jedisClient.set("SESSION:" + token, gson.toJson(user));
        //设置过期时间
        jedisClient.expire("SESSION:" + token,SESSION_EXPIRE);

        //把token返回,在Controller设置到cookie中
        return E3Result.ok(token);
    }
}
