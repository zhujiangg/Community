package com.nowcoder.community.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/2 10:40
 * @Version: 1.0
 * @Description: 业务层 切面：记录访问日志
 */
@Component
@Aspect
public class ServiceLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.nowcoder.community.community.service.*.*(..))")
    public void pointcut(){};

    /**
     * 日志中记录：用户[1.2.3.4] 在[x-x-x  x:x] 访问了 [com.nowcoder.community.service.xxx.xxx()]
     *  1、需要 ip：从 request 中获取
     *  2、需要 类名、方法名：从 joinPoint 中获取
     * */
    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        // 利用工具类获取 request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteHost();

        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String target = joinPoint.getSignature().getDeclaringTypeName() + '.' + joinPoint.getSignature().getName();
        logger.info(String.format("用户[%s],在[%s],访问了[%s].", ip, nowDate, target));
    }
}
