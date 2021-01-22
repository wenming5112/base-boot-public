package com.base.message.mail;

import com.base.common.exception.BusinessException;
import com.base.common.utils.PatternUtil;
import lombok.Data;

import java.util.Map;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/8 10:27
 **/
@Data
public class MailMessage extends BaseMailMessage {

    private MailMessage() {

    }

    public MailMessage(String receiver, String content) {
        this(null, "", receiver, content, null);
    }

    /**
     * @param mailTemplate 标题
     * @param username     用户名
     * @param receiver     接收者(邮箱)
     * @param content      内容(验证码)
     */
    public MailMessage(AbstractMailTemplate mailTemplate, String username, String receiver, String content) {
        this(mailTemplate, username, receiver, content, null);
    }


    public MailMessage(AbstractMailTemplate mailTemplate, String username, String receiver, String content, Map<String, Object> attachment) {
        if (!PatternUtil.verifyEmail(receiver)) {
            throw new BusinessException("邮件格式不正确!!");
        }
        this.mailTemplate = mailTemplate;
        this.username = username;
        this.receiver = receiver;
        this.content = content;
        this.attachment = attachment;
    }

    @Override
    public AbstractMailTemplate getMailTemplate() {
        return mailTemplate;
    }

    @Override
    public String getReceiver() {
        return receiver;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Map<String, Object> getAttachment() {
        return attachment;
    }

}
