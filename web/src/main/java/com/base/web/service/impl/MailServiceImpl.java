package com.base.web.service.impl;

import com.base.common.ApiResponse;
import com.base.common.utils.CommonUtil;
import com.base.common.utils.RedisCacheUtil;
import com.base.message.mail.*;
import com.base.message.mail.sender.HtmlMailSender;
import com.base.message.mail.sender.TemplateMailSender;
import com.base.message.mail.sender.TextMailSender;
import com.base.message.mail.sender.VerificationCodeTemplateMailSender;
import com.base.web.service.MailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/6 12:00
 **/
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private EmailProperties emailProperties;



    @Override
    public ApiResponse sendText(String receiver, String content) {
        BaseMailMessage message = new MailMessage(
                receiver,
                content
        );
        AbstractMailMessageSender messageSender = new TextMailSender(mailSender, emailProperties, message);
        messageSender.send();
        return ApiResponse.successful(10001, "发送成功");
    }

    @Override
    public ApiResponse sendHtml(String receiver, String content) {
        BaseMailMessage message = new MailMessage(
                receiver,
                content
        );
        AbstractMailMessageSender messageSender = new HtmlMailSender(mailSender, emailProperties, message);
        messageSender.send();
        return ApiResponse.successful(10001, "发送成功");
    }

    @Override
    public ApiResponse sendTemplate() {
        Context context = new Context();
        context.setVariable("", "");
        // 输入模板名称
        String content = templateEngine.process("", context);

        BaseMailMessage message = new MailMessage(
                "",
                content
        );

        AbstractMailMessageSender messageSender = new TemplateMailSender(mailSender, emailProperties, message);
        messageSender.send();
        return ApiResponse.successful(10001, "发送成功");
    }

    /**
     * 发送登录验证码
     *
     * @param username 用户名
     * @param receiver 接收者邮箱
     */
    @Override
    public ApiResponse sendForLogin(String receiver, String username) {
        AbstractMailTemplate mailTemplate = new MailMessageTemplate(
                emailProperties.getTemplate().getLogin().getSubject(),
                emailProperties.getTemplate().getLogin().getOperation()
        );

        this.sendTemplate(mailTemplate, username, receiver);
        return ApiResponse.successful(10001, "发送成功");
    }

    /**
     * 发送改密验证码
     *
     * @param username 用户名
     * @param receiver 接收者邮箱
     */
    @Override
    public ApiResponse sendForChangePassword(String receiver, String username) {
        AbstractMailTemplate mailTemplate = new MailMessageTemplate(
                emailProperties.getTemplate().getChangePassword().getSubject(),
                emailProperties.getTemplate().getChangePassword().getOperation()
        );

        this.sendTemplate(mailTemplate, username, receiver);
        return ApiResponse.successful(10001, "发送成功");
    }

    /**
     * 发送注册验证码
     *
     * @param username 用户名
     * @param receiver 接收者邮箱
     */
    @Override
    public ApiResponse sendForRegistry(String receiver, String username) {
        AbstractMailTemplate mailTemplate = new MailMessageTemplate(
                emailProperties.getTemplate().getRegistry().getSubject(),
                emailProperties.getTemplate().getRegistry().getOperation()
        );

        this.sendTemplate(mailTemplate, username, receiver);
        return ApiResponse.successful(10001, "发送成功");
    }

    @Async
    @Override
    public void sendTemplate(AbstractMailTemplate mailTemplate, String username, String receiver) {
        BaseMailMessage message = new MailMessage(
                mailTemplate,
                username,
                receiver,
                CommonUtil.getRandomNumCode(emailProperties.getTemplate().getCodeLength())
        );

        AbstractMailMessageSender messageSender = new VerificationCodeTemplateMailSender(mailSender, templateEngine, emailProperties, message);
        messageSender.send();
    }

}
