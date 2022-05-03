package com.nowcoder.community.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/2 10:27
 * @Version: 1.0
 * @Description: AOP 示例
 *      打印结果："环绕前"、"方法执行前"、"环绕后"、"方法执行后"、"afterRetuning"
 */
//@Component
//@Aspect
public class AlphaAspect {
    // 定义一个无用的切点
    // 返回值 组件.类.方法(参数)
    @Pointcut("execution(* com.nowcoder.community.community.service.*.*(..))")
    public void pointcut(){};

    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        System.out.println("方法执行前");
    }

    @After("pointcut()")
    public void after(){
        System.out.println("方法执行后");
    }

    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    // 连接点，等同于切入点，但能获取切入点的一些方法
    // @Around需传入 ProceedingJoinPoint，其他也能传入 JoinPoint
    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕前");
        Object proceed = joinPoint.proceed();
        System.out.println("环绕后");
        System.out.println(proceed);
    }
}
