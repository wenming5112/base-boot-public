package com.base.message.mail.sender;

import cn.hutool.core.collection.CollectionUtil;
import com.base.common.exception.BusinessException;
import com.base.common.utils.BusinessLoggerUtil;
import com.base.message.mail.AbstractMailMessageSender;
import com.base.message.mail.AbstractMailTemplate;
import com.base.message.mail.BaseMailMessage;
import com.base.message.mail.EmailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/8 11:18
 **/
public class HtmlMailSender extends AbstractMailMessageSender {
    private static final BusinessLoggerUtil log = BusinessLoggerUtil.newInstance(TextMailSender.class);

    private HtmlMailSender() {

    }

    public HtmlMailSender(JavaMailSender mailSender, EmailProperties emailProperties, BaseMailMessage message) {
        this.mailSender = mailSender;
        this.emailProperties = emailProperties;
        this.message = message;
    }

    @Override
    public void send() {
        AbstractMailTemplate mailTemplate = message.getMailTemplate();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailProperties.getTemplate().getFrom());
            helper.setTo(message.getReceiver());
            helper.setSubject(mailTemplate.getSubject());
            helper.setText(message.getContent(), true);
            // 判断是否有附加图片等
            if (CollectionUtil.isNotEmpty(message.getAttachment())) {
                message.getAttachment().forEach((key, value) -> {
                    try {
                        File file = new File(String.valueOf(value));
                        if (file.exists()) {
                            helper.addAttachment(key, new FileSystemResource(file));
                        }
                    } catch (MessagingException e) {
                        log.error(String.format("附件发送失败->message: %s", e.getMessage()));
                        throw new BusinessException("附件发送失败");
                    }
                });
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(String.format("模板邮件发送失败->message:%s", e.getMessage()));
            throw new BusinessException("邮件发送失败");
        }
    }
}
