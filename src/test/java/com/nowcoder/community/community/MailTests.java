package com.nowcoder.community.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import com.nowcoder.community.community.util.MailClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/19 14:27
 * @Version: 1.0
 * @Description: 邮件测试
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Test
    public void testMail(){
        mailClient.sendMail("zhujiangwander@163.com", "Test", "Welcome");
    }

    /**
     *  不使用 Controller 在 test 中调用 /mail/demo.html
     *      TemplateEngine templateEngine 扫描装配
     *      Context context 设置属性参数
     * */
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testHtml(){
        Context context = new Context();
        context.setVariable("username", "sunday");
        String content = templateEngine.process("/mail/demo", context);

        System.out.println(content);
        mailClient.sendMail("zhujiangwander@163.com", "HTML", content);
    }
}
