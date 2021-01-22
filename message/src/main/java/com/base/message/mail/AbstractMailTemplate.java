package com.base.message.mail;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/8 10:35
 **/
public abstract class AbstractMailTemplate {
    public String subject;
    public String operation;

    public abstract String getSubject();

    public abstract String getOperation();
}
