package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 操作日志 实体类
 *
 * @author ming
 * @since 2020/04/16
 */
@TableName("t_sys_operation_logger")
@Data
public class SysOperationLogger extends BaseEntity<SysOperationLogger> {

    private static final long serialVersionUID = 8239812536444495356L;
    /**
     * 操作内容
     */
    @TableField("operation_content")
    private String operationContent;
    /**
     * 操作类型( 0-未设置, 1-新增, 2-删除, 3-修改, 4-查询, 5-登录, 6-登出, 7-转账)
     */
    @TableField("operation_type")
    private Integer operationType;
    /**
     * 类名
     */
    @TableField("log_class_name")
    private String logClassName;
    /**
     * 方法名
     */
    @TableField("log_method_name")
    private String logMethodName;
    /**
     * 参数类型列表
     */
    @TableField("params_type_list")
    private String paramsTypeList;
    /**
     * 参数名称列表
     */
    @TableField("params_name_list")
    private String paramsNameList;
    /**
     * 参数值列表
     */
    @TableField("params_value_list")
    private String paramsValueList;
    /**
     * 异常信息
     */
    @TableField("exception_info")
    private String exceptionInfo;
    /**
     * 远程IP地址
     */
    @TableField("remote_ip_address")
    private String remoteIpAddress;
    /**
     * 访问类型(0-web, 1-android, 2-ios)
     */
    @TableField("access_type")
    private String accessType;
    /**
     * 执行时间
     */
    @TableField("exec_time")
    private String execTime;
    /**
     * 城市信息
     */
    @TableField("city_info")
    private String cityInfo;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
