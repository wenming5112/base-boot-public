package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限 实体类
 *
 * @author ming
 * @since 2020/04/16
 */
@TableName("t_sys_role_menu")
@Data
public class SysRoleMenu extends BaseEntity<SysRoleMenu> {

    private static final long serialVersionUID = 5460104780843016805L;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private String rid;

    /**
     * 菜单ID
     */
    @TableField("menu_id")
    private String mid;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
