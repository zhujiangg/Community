package com.nowcoder.community.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/24 19:46
 * @Version: 1.0
 * @Description: 拦截未登录的自定义注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
