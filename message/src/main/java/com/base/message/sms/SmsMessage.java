package com.base.message.sms;

import com.base.common.exception.BusinessException;
import com.base.common.utils.PatternUtil;
import lombok.Getter;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/7 10:27
 **/
@Getter
public class SmsMessage {

    private String receiver;
    private String code;

    private SmsMessage() {

    }

    public SmsMessage(String tel, String code) {
        if (!PatternUtil.verifyTel(tel)) {
            throw new BusinessException("");
        }
        this.receiver = tel;
        this.code = code;
    }
}
