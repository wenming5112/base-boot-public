package com.base.message.mail;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/7 15:04
 **/
public class MailMessageTemplate extends AbstractMailTemplate {

    private MailMessageTemplate() {

    }

    public MailMessageTemplate(String subject, String operation) {
        this.subject = subject;
        this.operation = operation;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getOperation() {
        return operation;
    }

}
