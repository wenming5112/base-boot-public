package com.base.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Sms Template enumeration
 *
 * @author ming
 * @version 1.0.0
 * @since 2020/04/16
 */
@Getter
@AllArgsConstructor
public enum SmsTemplateEnum {
    /**
     * 短信模板
     */
    IDENTITY(1, "SMS_177554270", "普通身份验证"),
    LOGIN(2, "SMS_177549209", "登录验证"),
    REGISTER(3, "SMS_177544231", "注册验证"),
    UPDATE_PASSWORD(4, "SMS_25195171", "修改密码"),
    ACTIVE_CONFIRM(5, "SMS_25195172", "激活确认"),
    LOGIN_ERROR(6, "SMS_25195174", "登录错误"),
    INFO_UPDATE(7, "SMS_25195170", "信息修改");

    private Integer id;
    private String code;
    private String description;
}
