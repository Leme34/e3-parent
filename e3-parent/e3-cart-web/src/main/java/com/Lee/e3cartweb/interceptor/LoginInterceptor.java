package com.Lee.e3cartweb.interceptor;

import com.Lee.dto.E3Result;
import com.Lee.pojo.TbUser;
import com.Lee.sso.service.TokenService;
import com.Lee.utils.CookieUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 根据用户是否登录进行不同处理的拦截器
 * 若缓存中查出已登录则把用户信息放入request域中(与过滤器后的handler使用的是同一request对象，取用户id作为redis中的field)，放行。
 * 否则直接放行
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Reference
    private TokenService tokenService;

    // 前处理，执行handler之前执行此方法。
    //返回true，放行	   false：拦截
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //从cookie中取得token
        String token = CookieUtils.getCookieValue(request, "token");
        //根据token查询缓存是否有该用户的用户信息
        E3Result e3Result = tokenService.getUserByToken(token);
        //若缓存中有用户信息，则放入request域中
        if (e3Result.getStatus() == 200) {
            TbUser user = (TbUser) e3Result.getData();
            request.setAttribute("user", user);
        }
        return true;
    }

    //handler执行之后，返回ModeAndView之前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //完成处理，返回ModelAndView之后。
    //可以在此处理异常
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
