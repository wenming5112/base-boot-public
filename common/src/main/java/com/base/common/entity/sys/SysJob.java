package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 定时任务 实体类
 *
 * @author ming
 * @since 2020/04/16
 */
@TableName("t_sys_job")
@Data
public class SysJob extends BaseEntity<SysJob> {

    /**
     * 状态
     */
    public static final String STATUS_RUNNING = "1";
    public static final String STATUS_NOT_RUNNING = "0";
    public static final String CONCURRENT_IS = "1";
    public static final String CONCURRENT_NOT = "0";
    private static final long serialVersionUID = 7241871702214079052L;

    /**
     * 任务名
     */
    @TableField("job_name")
    private String jobName;
    /**
     * 任务分组
     */
    @TableField("job_group")
    private String jobGroup;
    /**
     * Cron表达式
     */
    @TableField("cron")
    private String cron;
    /**
     * 执行状态()
     */
    @TableField("concurrent")
    private String concurrent;
    /**
     * 执行类
     */
    @TableField("bean_class")
    private String beanClass;
    /**
     * 执行方法
     */
    @TableField("method_name")
    private String methodName;
    /**
     * 初始化参数
     */
    @TableField("params")
    private String params;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
