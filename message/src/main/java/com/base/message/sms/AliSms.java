package com.base.message.sms;


import com.base.message.sms.template.AliTemplate;

/**
 * Ali yun sms
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/5 14:45
 **/
public class AliSms {
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

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(String codeLength) {
        this.codeLength = codeLength;
    }

    public AliTemplate getTemplate() {
        return template;
    }

    public void setTemplate(AliTemplate template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "AliSms{" +
                "accessKey='" + accessKey + '\'' +
                ", accessSecret='" + accessSecret + '\'' +
                ", signName='" + signName + '\'' +
                ", domain='" + domain + '\'' +
                ", version='" + version + '\'' +
                ", action='" + action + '\'' +
                ", regionId='" + regionId + '\'' +
                ", productName='" + productName + '\'' +
                ", codeLength='" + codeLength + '\'' +
                '}';
    }
}
