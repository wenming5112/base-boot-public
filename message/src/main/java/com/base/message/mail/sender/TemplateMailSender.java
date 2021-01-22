package com.base.message.mail.sender;

import com.base.common.exception.BusinessException;
import com.base.common.utils.BusinessLoggerUtil;
import com.base.message.mail.AbstractMailMessageSender;
import com.base.message.mail.AbstractMailTemplate;
import com.base.message.mail.BaseMailMessage;
import com.base.message.mail.EmailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/7 18:07
 **/
public class TemplateMailSender extends AbstractMailMessageSender {

    private static final BusinessLoggerUtil log = BusinessLoggerUtil.newInstance(TextMailSender.class);

    private TemplateMailSender() {

    }

    public TemplateMailSender(JavaMailSender mailSender, EmailProperties emailProperties, BaseMailMessage message) {
        this.mailSender = mailSender;
        this.emailProperties = emailProperties;
        this.message = message;
    }

    @Override
    public void send() {
        AbstractMailTemplate template = message.getMailTemplate();
        String receiver = message.getReceiver();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailProperties.getTemplate().getFrom());
            helper.setTo(receiver);
            helper.setSubject(template.getSubject());
            helper.setText(message.getContent(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            redisCache.delete(receiver);
            log.error(String.format("模板邮件发送失败->message: %s", e.getMessage()));
            throw new BusinessException("邮件发送失败");
        }
    }
}


