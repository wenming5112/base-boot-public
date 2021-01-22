package com.base.common.exception;

import com.base.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理
 *
 * @author ming
 * @since 2020/04/18
 */
@Slf4j
@Order()
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
        return ApiResponse.failed(10001, e.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> businessExceptionHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
        return ApiResponse.failed(10001, e.getMessage());
    }

    /**
     * 接口限流 异常处理(实体对象传参)
     *
     * @param e RequestLimitException
     * @return ApiResult
     */
    @ExceptionHandler(value = RequestLimitException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<String> requestLimitExceptionHandler(RequestLimitException e) {
        e.printStackTrace();
        log.error(String.format(">>>--- RequestLimit exception msg: %s", e.getMessage()), e);
        return ApiResponse.failed(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    /**
     * 参数校验 异常处理(实体对象传参)
     *
     * @param e BindException
     * @return ApiResult
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        log.error(String.format(">>>--- MethodArgumentNotValid exception msg: %s", fieldErrors.get(0).getDefaultMessage()), fieldErrors.get(0));
        return ApiResponse.failed(10001, fieldErrors.get(0).getDefaultMessage());
    }
}
