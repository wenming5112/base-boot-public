package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户 实体类
 *
 * @author ming
 * @since 2020/04/16
 */
@TableName("t_sys_user")
@Data
@ToString
@Accessors(chain = true)
public class SysUser extends BaseEntity<SysUser> implements UserDetails {

    private static final long serialVersionUID = 8217603031866514703L;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 性别(1:男 0:女)
     * default null
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 证件类型(普通居民身份证/护照/港澳台地区证件)
     */
    @TableField("certificate_type")
    private String certificateType;

    /**
     * 证件编号
     */
    @TableField("certificate_number")
    private String certificateNumber;

    /**
     * 手机号码
     */
    @TableField("telephone")
    private String telephone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 状态ID
     */
    @TableField("status_id")
    private String statusId;

    /**
     * 操作状态
     */
    @TableField("operation_status")
    private Integer operationStatus;

    /**
     * 登录IP
     */
    @TableField("login_ip")
    private String loginIp;

    /**
     * 登录地址
     */
    @TableField("login_address")
    private String loginAddress;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    private Set<SysRole> roles;

    /**
     * 账户是否过期
     */
    @TableField(exist = false)
    private Boolean accountNonExpired = true;

    /**
     * 账户是否锁定
     */
    @TableField(exist = false)
    private Boolean accountNonLocked = true;

    /**
     * 密码是否过期
     */
    @TableField(exist = false)
    private Boolean credentialsNonExpired = true;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @TableField(exist = false)
    private Set<GrantedAuthority> authorities = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        for (SysRole role : roles) {
            // 角色也是权限的一种，是比较特殊的一类权限
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
            for (SysMenu menu : role.getMenus()) {
                authorities.add(new SimpleGrantedAuthority(menu.getPermission()));
            }
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return valid;
    }
}
