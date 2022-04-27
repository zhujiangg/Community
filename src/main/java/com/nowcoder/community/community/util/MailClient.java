package com.nowcoder.community.community.util;

import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/19 14:09
 * @Version: 1.0
 * @Description: 邮件发送
 */

@Component
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);   //支持html文本
            mailSender.send(messageHelper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败" + e.getMessage());
        }
    }
}
