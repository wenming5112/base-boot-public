package com.base.message.mail.sender;

import com.base.common.exception.BusinessException;
import com.base.common.utils.BusinessLoggerUtil;
import com.base.message.mail.AbstractMailMessageSender;
import com.base.message.mail.AbstractMailTemplate;
import com.base.message.mail.BaseMailMessage;
import com.base.message.mail.EmailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/6 18:07
 **/

public class TextMailSender extends AbstractMailMessageSender {
    private static final BusinessLoggerUtil log = BusinessLoggerUtil.newInstance(TextMailSender.class);

    private TextMailSender() {

    }

    public TextMailSender(JavaMailSender mailSender, EmailProperties emailProperties, BaseMailMessage message) {
        this.mailSender = mailSender;
        this.emailProperties = emailProperties;
        this.message = message;
    }

    @Override
    public void send() {
        AbstractMailTemplate template = message.getMailTemplate();
        String content = message.getContent();
        String to = message.getReceiver();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailProperties.getTemplate().getFrom());
            message.setTo(to);
            message.setSubject(template.getSubject());
            message.setText(content);
            mailSender.send(message);
        } catch (MailException e) {
            log.error(String.format("模板邮件发送失败->message: %s", e.getMessage()));
            throw new BusinessException("邮件发送失败");
        }
    }

}
