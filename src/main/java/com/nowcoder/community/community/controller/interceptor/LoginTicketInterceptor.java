package com.nowcoder.community.community.controller.interceptor;

import com.nowcoder.community.community.entity.LoginTicket;
import com.nowcoder.community.community.entity.User;
import com.nowcoder.community.community.service.UserService;
import com.nowcoder.community.community.util.CookieUtil;
import com.nowcoder.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/22 10:14
 * @Version: 1.0
 * @Description: 登录拦截器，显示登录信息
 *                  1、preHandle：在请求开始时查询登录用户、在请求中持有用户数据
 *                  2、postHandle：在模板视图上显示用户数据
 *                  3、afterCompletion：请求结束时清理用户数据
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    /**
     *     在Controller之前执行
     *     @Override
     *     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
     *         return HandlerInterceptor.super.preHandle(request, response, handler);
     *     }
     *     在Controller之后执行
     *     @Override
     *     public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
     *         HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
     *     }
     *     在TemplateEngine之后执行
     *     @Override
     *     public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
     *         HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
     *     }
     */
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取 ticket
        String ticket = CookieUtil.getValue(request, "ticket");
        if(ticket != null){
            // 根据 ticket 查询 登录凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户，并发条件下多客户端请求用户需保证 User 隔离性（ThreadLocal 实现）
                hostHolder.setUser(user);
            }
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
