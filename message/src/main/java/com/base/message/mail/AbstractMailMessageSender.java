package com.base.message.mail;

import com.base.common.utils.RedisCacheUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/8 10:19
 **/
public abstract class AbstractMailMessageSender {

    protected RedisCacheUtil redisCache;

    protected EmailProperties emailProperties;

    protected JavaMailSender mailSender;

    protected TemplateEngine templateEngine;

    protected BaseMailMessage message;

    public abstract void send();
}
