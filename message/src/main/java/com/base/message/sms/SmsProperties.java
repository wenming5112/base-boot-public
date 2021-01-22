package com.base.message.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sms config
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/5 14:45
 **/
@ConfigurationProperties(prefix = "sms")
@Component
@Data
public class SmsProperties {
    private AliSms ali;
}
