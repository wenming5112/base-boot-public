package com.base.message.mail;


import com.base.message.mail.template.ChangePasswordMailTemplate;
import com.base.message.mail.template.LoginMailTemplate;
import com.base.message.mail.template.RegistryMailTemplate;

/**
 * 邮件模板
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/5 18:53
 **/
public class EmailTemplate {
    private String name;
    private String from;
    private Integer codeLength;
    private Long effectiveTime;
    private String contactInfo;
    private LoginMailTemplate login;
    private RegistryMailTemplate registry;
    private ChangePasswordMailTemplate changePassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(Integer codeLength) {
        this.codeLength = codeLength;
    }

    public Long getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Long effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LoginMailTemplate getLogin() {
        return login;
    }

    public void setLogin(LoginMailTemplate login) {
        this.login = login;
    }

    public RegistryMailTemplate getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryMailTemplate registry) {
        this.registry = registry;
    }

    public ChangePasswordMailTemplate getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(ChangePasswordMailTemplate changePassword) {
        this.changePassword = changePassword;
    }
}
