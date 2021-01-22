package com.base.web.entity.vo;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 角色菜单VO
 *
 * @author ming
 * @since 2019:08:31 11:03
 */
@Data
@ApiModel(value = "角色菜单VO")
@Accessors(chain = true)
public class SysMenuVO implements Serializable {

    private static final long serialVersionUID = 5812586427733767590L;
    @ApiModelProperty(value = "菜单ID")
    private String menuId;

    @ApiModelProperty(value = "菜单标题名")
    private String title;

    @ApiModelProperty(value = "前端路由")
    private String path;

    @ApiModelProperty(value = "前端组件路径")
    private String component;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "访问权限")
    private String permission;

    @ApiModelProperty(value = "父id")
    private String pid;

    @ApiModelProperty(value = "类型(0-按钮 1-菜单)")
    private Integer type;

    @ApiModelProperty(value = "菜单描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private String modifyTime;
}
