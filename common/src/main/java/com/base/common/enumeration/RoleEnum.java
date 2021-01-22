package com.base.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/26 23:35
 **/
@Getter
@AllArgsConstructor
public enum RoleEnum {
    // 角色枚举
    ADMIN("admin", "5a17ff45d8d4436ea6bcc434ce2f7e72"),
    USER("user", "729b2f1bab2e43f3b55d42b1950f20a4");

    private String name;
    private String rid;
}
