package com.base.message.mail;

import io.swagger.annotations.ApiModel;

import java.util.Map;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/7 10:25
 **/
@ApiModel(value = "邮件信息")
public abstract class BaseMailMessage {

    String receiver;
    String username;
    String content;
    Map<String, Object> attachment;
    AbstractMailTemplate mailTemplate;

    public abstract AbstractMailTemplate getMailTemplate();

    public abstract String getReceiver();

    public abstract String getUsername();

    public abstract String getContent();

    public abstract Map<String, Object> getAttachment();
}
