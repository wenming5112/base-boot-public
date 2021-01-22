package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 状态 实体类
 *
 * @author ming
 * @since 2020/04/15
 */
@TableName("t_sys_status")
@Data
public class SysStatus extends BaseEntity<SysStatus> {

    private static final long serialVersionUID = -6200059596160712595L;
    /**
     * 状态编码
     */
    @TableField("status_code")
    private Integer statusCode;
    /**
     * 状态
     */
    @TableField("status_name")
    private String statusName;
    /**
     * 描述
     */
    @TableField("description")
    private String description;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
