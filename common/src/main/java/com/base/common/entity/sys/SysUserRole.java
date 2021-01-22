package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 角色 实体类
 *
 * @author ming
 * @since 2020/04/16
 */
@TableName("t_sys_user_role")
@Data
@ToString
public class SysUserRole extends BaseEntity<SysUserRole> {

    private static final long serialVersionUID = 2833283339748745991L;

    /**
     * 后台用户ID
     */
    @TableField("user_id")
    private String uid;

    /**
     * 后台角色ID
     */
    @TableField("role_id")
    private String rid;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
