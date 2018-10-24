package com.Lee.e3ssoweb.controller;

import com.Lee.dto.E3Result;
import com.Lee.sso.service.LoginService;
import com.Lee.utils.CookieUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录处理
 */
@Controller
public class LoginController {

    @Value("${COOKIE_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;

    @Reference
    private LoginService loginService;

    @RequestMapping(value="/user/login", method= RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password,
                          HttpServletRequest request, HttpServletResponse response) {

        E3Result tokenResult = loginService.userLogin(username, password);
        //若登录成功
        if (tokenResult.getStatus()==200){
            //把token(即sessionId)设置到cookie中
            CookieUtils.setCookie(request,response,COOKIE_TOKEN_KEY,tokenResult.getData().toString());
        }
        return tokenResult;
    }

}
