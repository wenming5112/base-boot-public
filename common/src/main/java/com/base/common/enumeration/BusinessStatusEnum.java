package com.base.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Business status
 *
 * @author ming
 * @version 1.0.0
 * @since 2020/04/16
 */
@Getter
@AllArgsConstructor
public enum BusinessStatusEnum {

    /**
     * 业务响应枚举
     */
    NORMAL(1, 10000, "NORMAL", "正常"),
    INVALID(2, 10001, "INVALID", "无效"),
    LOCK(3, 10002, "LOCK", "已锁定"),
    NO_ACTIVE(4, 10003, "DISABLED", "未激活"),
    NO_CONFIG(5, 10004, "NO_CONFIG", "未配置"),
    RUNNING(6, 10005, "RUNNING", "运行中"),
    STOPPED(7, 10006, "STOPPED", "已停止"),
    CONFIGURED(8, 10007, "CONFIGURED", "已配置"),
    ACTIVATED(9, 10008, "ACTIVATED", "已激活"),
    PENDING(10, 10009, "PENDING", "待审批"),
    CONFIGURING(14, 10013, "CONFIGURING", "配置中"),
    AGREED(15, 100014, "AGREED", "已同意"),
    BUILDING_CONTAINER(16, 100015, "BUILDING", "正在构建容器"),
    RUNNING_CONTAINER(17, 100016, "RUNNING_CONTAINER", "正在运行容器"),
    ACTIVATING(18, 100017, "ACTIVATING", "激活中"),
    INSTALLING(19, 100018, "INSTALLING", "正在安装"),
    INSTANTIATING(20, 100019, "INSTANTIATING", "正在实例化"),
    UPGRADING(21, 100020, "UPGRADING", "正在升级");

    private int id;
    private int code;
    private String enName;
    private String zhName;
}
