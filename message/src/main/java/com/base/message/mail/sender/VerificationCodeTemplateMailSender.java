package com.base.message.mail.sender;

import com.base.common.exception.BusinessException;
import com.base.common.utils.BusinessLoggerUtil;
import com.base.common.utils.RedisCacheUtil;
import com.base.common.utils.SpringContextUtil;
import com.base.message.mail.AbstractMailMessageSender;
import com.base.message.mail.AbstractMailTemplate;
import com.base.message.mail.BaseMailMessage;
import com.base.message.mail.EmailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/7 18:07
 **/
public class VerificationCodeTemplateMailSender extends AbstractMailMessageSender {

    private static final BusinessLoggerUtil log = BusinessLoggerUtil.newInstance(TextMailSender.class);

    private static final String DATA_KEY_USERNAME = "username";
    private static final String DATA_KEY_CODE = "code";
    private static final String DATA_KEY_TYPE = "type";
    private static final String DATA_KEY_CONTACT_INFO = "contact_info";
    private static final String DATA_KEY_EFFECTIVE_TIME = "effective_time";

    private VerificationCodeTemplateMailSender() {

    }

    public VerificationCodeTemplateMailSender(JavaMailSender mailSender, TemplateEngine templateEngine, EmailProperties emailProperties, BaseMailMessage message) {
        this.redisCache = SpringContextUtil.getBean(RedisCacheUtil.class);
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.emailProperties = emailProperties;
        this.message = message;
    }

    @Override
    public void send() {
        AbstractMailTemplate template = message.getMailTemplate();
        String receiver = message.getReceiver();
        String username = message.getUsername();
        String code = message.getContent();
        if (redisCache.hasKey(receiver)) {
            throw new BusinessException("验证码未过期，请勿重复发送!!");
        }
        redisCache.setString(receiver, code, emailProperties.getTemplate().getEffectiveTime() * 60);
        if (redisCache.hasKey(receiver)) {
            Context context = new Context();
            context.setVariable(DATA_KEY_USERNAME, username);
            context.setVariable(DATA_KEY_CODE, code);
            context.setVariable(DATA_KEY_TYPE, template.getOperation());
            context.setVariable(DATA_KEY_CONTACT_INFO, emailProperties.getTemplate().getContactInfo());
            context.setVariable(DATA_KEY_EFFECTIVE_TIME, emailProperties.getTemplate().getEffectiveTime());
            String content = templateEngine.process(emailProperties.getTemplate().getName(), context);
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(emailProperties.getTemplate().getFrom());
                helper.setTo(receiver);
                helper.setSubject(template.getSubject());
                helper.setText(content, true);
                mailSender.send(mimeMessage);
            } catch (MessagingException e) {
                redisCache.delete(receiver);
                log.error(String.format("模板邮件发送失败->message: %s", e.getMessage()));
                throw new BusinessException("邮件发送失败");
            }
        } else {
            throw new BusinessException("邮件发送失败");
        }
    }

}
