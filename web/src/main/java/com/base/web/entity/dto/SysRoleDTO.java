package com.base.web.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author ming
 * @version 1.0.0
 * @date 2019 15:25
 */
@Data
@ApiModel(value = "后台角色DTO")
public class SysRoleDTO implements Serializable {

    private final static long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    private String rid;

    @ApiModelProperty(value = "角色名")
    @NotBlank(message = "角色名不能为空")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

}
