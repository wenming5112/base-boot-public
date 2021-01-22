package com.base.message;


import com.base.message.mail.*;
import com.base.message.mail.sender.TextMailSender;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

class LoginVerificationCodeTest {

    @Resource
    private JavaMailSender mailSender;
    @Resource
    private EmailProperties emailProperties;

    @Test
    void demo() {
        AbstractMailTemplate mailTemplate = new MailMessageTemplate("", "");
        BaseMailMessage message = new MailMessage(mailTemplate, "", "", "");

        AbstractMailMessageSender messageSender = new TextMailSender(mailSender, emailProperties, message);

        messageSender.send();
    }

}
