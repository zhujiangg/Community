package com.nowcoder.community.community.controller.interceptor;

import com.nowcoder.community.community.annotation.LoginRequired;
import com.nowcoder.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/24 19:51
 * @Version: 1.0
 * @Description: 未登录恶意访问拦截器
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 判断拦截的是否是方法，是的话获取这个拦截的方法 method，
         * 获取 method的自定义注解，由于这个注解作用于 getSettingPag() 和 uploadHeader()
         * 如果返回不为 null，说明访问了这两个方法。
         * 如果此时 user为 null，说明未登录，属于非法访问，需重定向登录页面
         */

        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if(loginRequired != null && hostHolder.getUser() == null){
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
