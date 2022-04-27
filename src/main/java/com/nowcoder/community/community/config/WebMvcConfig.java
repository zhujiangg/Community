package com.nowcoder.community.community.config;

import com.nowcoder.community.community.controller.interceptor.AlphaInterceptor;
import com.nowcoder.community.community.controller.interceptor.LoginRequiredInterceptor;
import com.nowcoder.community.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/22 10:20
 * @Version: 1.0
 * @Description: 拦截器配置类
 *                  将各拦截器装进 registry中，设置拦截路径
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // <测试 registry>
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                .addPathPatterns("/register", "/login");

        // 获取登录信息
        registry.addInterceptor(loginTicketInterceptor).excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        // 禁止恶意访问（除了拦截静态资源还要拦截 自定义注解作用的方法）
        registry.addInterceptor(loginRequiredInterceptor).excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
