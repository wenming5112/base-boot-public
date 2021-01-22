package com.base.message.provider;

import com.base.message.MessageServiceProvider;
import com.base.message.sms.template.AliTemplate;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/7 10:51
 **/
public class AliSmsProvider implements MessageServiceProvider {
    private String accessKey;
    private String accessSecret;
    private String signName;
    private String domain;
    private String version;
    private String action;
    private String regionId;
    private String productName;
    private String codeLength;
    private AliTemplate template;

    @Override
    public String getAccessKey() {
        return null;
    }

    @Override
    public String getAccessSecret() {
        return null;
    }

    @Override
    public AliTemplate getTemplate() {
        return null;
    }
}
