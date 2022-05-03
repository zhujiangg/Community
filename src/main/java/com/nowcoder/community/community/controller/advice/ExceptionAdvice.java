package com.nowcoder.community.community.controller.advice;

import com.nowcoder.community.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @Author: ZhuJiang
 * @Date: 2022/5/1 14:07
 * @Version: 1.0
 * @Description: 统一处理全局异常，所有层的异常都会汇集在 Controller，在此处理即可
 */

@ControllerAdvice(annotations = Controller.class)   // 表示该类是Controller的全局配置类；且注解只对 Controller下的 bean有效
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     *  1、在日志中按序记录异常
     *  2、根据异常类型做不同处理
     *      若请求是 XML格式，说明是异步请求（Json），应设置文本类型（Json）后写入错误信息
     *      若是 html格式说明是普通请求，应重定向至错误页面 500.html
     * */
    @ExceptionHandler({Exception.class}) // 处理捕获到的异常；且Exception.class为所有异常父类 即所有异常都在这处理
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常: " + e.getMessage());
        for (StackTraceElement element: e.getStackTrace()) {
            logger.error(element.toString());
        }

        String xRequestedWith = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(xRequestedWith)){
            // 设置浏览器响应类型为plain （普通字符串）或 Json
            response.setContentType("application/plain;charset=utf-8");
            // 打印输出流打印错误信息，会在异步错误的时候弹出此错误信息（此错误信息存在Json中）
            // print()不仅可以打印输出文本格式的（包括html标签），还能打印对象，而 writer()不行
            response.getWriter().println(CommunityUtil.getJsonString(1, "服务器异常!!!"));
        }else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
