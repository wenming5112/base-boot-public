package com.base.common.exception;

/**
 * 限流异常
 *
 * @author ming
 * @version 1.0.0
 * @since 2020/04/16
 */
public class RequestLimitException extends BusinessException {

    private static final long serialVersionUID = 1501118347734700923L;

    public RequestLimitException() {
        super("系统繁忙,请稍候再试!");
    }

    public RequestLimitException(String message) {
        super(message);
    }
}
