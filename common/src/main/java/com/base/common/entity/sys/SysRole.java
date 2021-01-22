package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Set;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/5 11:34
 **/
@TableName("t_sys_role")
@Data
@ToString
public class SysRole extends BaseEntity<SysRole> implements GrantedAuthority {

    private static final long serialVersionUID = 6369725038374818311L;
    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色描述
     */
    @TableField("role_desc")
    private String roleDesc;
    /**
     * 状态ID
     */
    @TableField("status_id")
    private String statusId;

    @TableField(exist = false)
    private Set<SysMenu> menus;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String getAuthority() {
        return roleName;
    }
}
