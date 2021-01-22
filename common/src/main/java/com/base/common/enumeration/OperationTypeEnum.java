package com.base.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举
 *
 * @author ming
 * @version 1.0.0
 * @since 2020/4/16 10:49
 **/
@Getter
@AllArgsConstructor
public enum OperationTypeEnum {
    /**
     * 日志类型枚举
     */
    OPERATION_TYPE_CREATE(1, "新增"),
    OPERATION_TYPE_DELETE(2, "删除"),
    OPERATION_TYPE_UPDATE(3, "修改"),
    OPERATION_TYPE_SELECT(4, "查询"),
    OPERATION_TYPE_LOGIN(5, "登录"),
    OPERATION_TYPE_LOGOUT(6, "登出"),
    OPERATION_TYPE_TRANSFER(7, "转账"),
    OPERATION_TYPE_REGISTER(8, "注册"),
    OPERATION_TYPE_VERIFICATION(9, "验证");

    private Integer id;
    private String type;
}
