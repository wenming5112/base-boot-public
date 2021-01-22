package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * 菜单
 *
 * @author ming
 * @version 1.0.0
 * @since 2020/12/31 1:04
 **/
@TableName("t_sys_menu")
@Data
@ToString
public class SysMenu extends BaseEntity<SysMenu> implements Authentication {

    private static final long serialVersionUID = 4814002326662320464L;

    /**
     * 标题(前端)
     */
    @TableField("title")
    private String title;

    /**
     * 路由图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 路径(前端)
     */
    @TableField("path")
    private String path;

    /**
     * 组件(前端)
     */
    @TableField("component")
    private String component;

    /**
     * 权限(后端)
     */
    @TableField("permission")
    private String permission;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 1:菜单 0:按钮
     * default 1
     */
    @TableField("type")
    private Integer type;

    /**
     * 父级ID
     */
    @TableField("parent_id")
    private String parentId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {

    }

    /**
     * Returns the name of this principal.
     *
     * @return the name of this principal.
     */
    @Override
    public String getName() {
        return null;
    }
}
