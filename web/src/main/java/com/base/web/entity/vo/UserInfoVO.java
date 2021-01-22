package com.base.web.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户信息VO
 *
 * @author ming
 * @version 1.0.0
 * @date 2019 10:30
 */
@Data
@ToString
@ApiModel(value = "用户信息VO")
@JsonIgnoreProperties(value = "handler")
@Accessors(chain = true)
public class UserInfoVO extends UserLoginVO implements Serializable {

    private static final long serialVersionUID = 4340499357347542307L;

    @ApiModelProperty(value = "jwtToken", hidden = true)
    private String jwtToken;

}
