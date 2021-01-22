package com.base.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ming
 * @version 1.0.0
 * @since 2020/12/30 22:58
 **/

@Getter
@AllArgsConstructor
public enum ResponseMessageEnum {

    CREATE_SUCCESS(10001, "新增成功"),
    CREATE_FAILED(10002, "新增失败"),
    UPDATE_SUCCESS(10003, "修改成功"),
    UPDATE_FAILED(10004, "修改失败"),
    DELETE_SUCCESS(10005, "删除成功"),
    DELETE_FAILED(10006, "删除失败"),
    USER_NOT_EXIST(10800, "用户不存在"),
    OPERATION_SUCCESS(10200, "操作成功"),
    TOKEN_IS_NULL(10501, "Token为空"),
    USER_TOKEN_INVALID(10502, "无效的Token"),
    USERNAME_OR_PASSWORD_ERROR(10503, "用户名或密码错误"),
    USER_ALREADY_EXIST(10503, "用户已存在"),
    USER_LOGIN(10011, "登出成功!!"),
    USER_LOGOUT_SUCCESS(10011, "登录成功!!"),
    RESULT_CODE_LOGIN_IS_OVERDUE(207, "登录已失效"),
    OPERATION_FAILED(10500, "操作失败");

    private Integer code;
    private String msg;
}
