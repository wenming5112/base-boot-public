package com.base.web.service;

import com.base.common.ApiResponse;
import com.base.message.mail.AbstractMailTemplate;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/6 9:44
 **/
public interface MailService {

    /**
     * 发送登录验证码
     *
     * @param username 用户名
     * @param receiver 接收者邮箱
     */
    ApiResponse sendForLogin(String receiver, String username);

    /**
     * 发送改密验证码
     *
     * @param username 用户名
     * @param receiver 接收者邮箱
     */
    ApiResponse sendForChangePassword(String receiver, String username);

    /**
     * 发送注册验证码
     *
     * @param username 用户名
     * @param receiver 接收者邮箱
     */
    ApiResponse sendForRegistry(String receiver, String username);

    ApiResponse sendText(String receiver, String content);

    ApiResponse sendHtml(String receiver, String content);

    ApiResponse sendTemplate();

    void sendTemplate(AbstractMailTemplate mailTemplate, String username, String receiver);

}
