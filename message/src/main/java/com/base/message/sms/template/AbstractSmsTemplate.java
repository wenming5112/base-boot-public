package com.base.message.sms.template;

import lombok.Data;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/6 14:08
 **/
@Data
public abstract class AbstractSmsTemplate {
    /**
     * 模板代码
     */
    protected String code;
    /**
     * 标题
     */
    protected String subject;
}
