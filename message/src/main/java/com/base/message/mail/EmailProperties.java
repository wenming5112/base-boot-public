package com.base.message.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * email config
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/11/27 15:36
 **/

@ConfigurationProperties(prefix = "email")
@Configuration
public class EmailProperties {
    private EmailTemplate template;

    public EmailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EmailTemplate template) {
        this.template = template;
    }
}
