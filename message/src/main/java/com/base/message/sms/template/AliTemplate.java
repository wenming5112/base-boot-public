package com.base.message.sms.template;

import lombok.Data;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/6 14:13
 **/
@Data
public class AliTemplate {
    private LoginTemplate login;
    private RegistryTemplate registry;
}
