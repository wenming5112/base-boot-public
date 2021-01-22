package com.base.common;


import com.base.common.enumeration.ResponseMessageEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Global Return
 *
 * @author ming
 * @version 1.0.0
 * @since 2020/4/14 14:30
 **/
@Data
@ApiModel("统一响应类")
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 9024462031856060619L;
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("信息")
    private String msg;
    @ApiModelProperty("数据")
    private T data;

    private ApiResponse() {
        super();
    }

    public static <T> ApiResponse<T> successful(Integer code, String msg) {
        return getApiResponseWithoutData(code, msg);
    }

    public static <T> ApiResponse<T> successful(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ResponseMessageEnum.OPERATION_SUCCESS.getCode());
        apiResponse.setMsg("");
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> ApiResponse<T> successful(Integer code, String msg, T data) {
        return getApiResponseWithData(code, msg, data);
    }

    public static <T> ApiResponse<T> failed(Integer code, String msg) {
        return getApiResponseWithoutData(code, msg);
    }

    private static <T> ApiResponse<T> getApiResponseWithoutData(Integer code, String msg) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMsg(msg);
        apiResponse.setData(null);
        return apiResponse;
    }

    private static <T> ApiResponse<T> getApiResponseWithData(Integer code, String msg, T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMsg(msg);
        apiResponse.setData(data);
        return apiResponse;
    }
}

