package com.base.message;

import com.base.message.sms.template.AliTemplate;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/6 18:13
 **/
public interface MessageServiceProvider {

    String getAccessKey();

    String getAccessSecret();

    AliTemplate getTemplate();

}
