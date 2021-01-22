package com.base.message.mail.template;

import lombok.Data;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/6 13:38
 **/

@Data
public abstract class BaseMailTemplate {
    protected String subject;
    protected String operation;
}
