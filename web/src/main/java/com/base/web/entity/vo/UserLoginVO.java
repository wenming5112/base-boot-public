package com.base.web.entity.vo;

import cn.hutool.core.date.DatePattern;
import com.base.common.entity.TreeNode;
import com.base.common.entity.sys.SysMenu;
import com.base.common.entity.sys.SysRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ming
 * @date 2019:08:26 16:14
 */
@Data
@ToString
@ApiModel(value = "用户信息VO")
@Accessors(chain = true)
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = -3468247231212840519L;
    @ApiModelProperty(value = "用户ID")
    protected String uid;

    @ApiModelProperty(value = "用户名")
    protected String username;

    @ApiModelProperty(value = "性别")
    protected String sex;

    @ApiModelProperty(value = "证件类型")
    protected String certificateType;

    @ApiModelProperty(value = "证件编号")
    protected String certificateNumber;

    @ApiModelProperty(value = "手机号码")
    protected String telephone;

    @ApiModelProperty(value = "邮箱")
    protected String email;

    @ApiModelProperty(value = "状态")
    protected Integer userStatus;

    @ApiModelProperty(value = "操作状态")
    protected Integer operationStatus;

    @ApiModelProperty(value = "登录IP")
    private String loginIp;

    @ApiModelProperty(value = "登录地址")
    private String loginAddress;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "角色集")
    protected Set<SysRole> roles;

    @ApiModelProperty(value = "菜单集")
    protected Set<SysMenu> menus;

    @ApiModelProperty(value = "用户路由")
    private Set<TreeNode<SysMenu>> userRoutes;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    protected String createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    protected String modifyTime;

    public void setMyMenus(Set<SysRole> roles) {
        Set<SysMenu> menusSet = new HashSet<>();
        for (SysRole role : roles) {
            if (!ObjectUtils.isEmpty(role.getMenus())) {
                menusSet.addAll(role.getMenus());
            }
        }
        this.menus = menusSet;
    }
}
