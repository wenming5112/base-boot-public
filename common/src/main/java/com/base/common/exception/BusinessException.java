package com.base.common.exception;

import lombok.Getter;

/**
 * @author ming
 * @version 1.0.0
 * @since 2020/12/30 23:05
 **/

@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 2689211331447847927L;
    private String code;

    private String msg;

    public BusinessException() {
        super();
    }

    public BusinessException(BaseExceptionInterface exceptionInterface) {
        super(exceptionInterface.getCode());
        this.code = exceptionInterface.getCode();
        this.msg = exceptionInterface.getMsg();
    }

    public BusinessException(BaseExceptionInterface exceptionInterface, Throwable cause) {
        super(exceptionInterface.getCode(), cause);
        this.code = exceptionInterface.getCode();
        this.msg = exceptionInterface.getMsg();
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.code = "-1";
        this.msg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg) {
        super(errorCode);
        this.code = errorCode;
        this.msg = errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
