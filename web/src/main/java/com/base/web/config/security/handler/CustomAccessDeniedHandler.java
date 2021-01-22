package com.base.web.config.security.handler;


import com.base.common.ApiResponse;
import com.base.web.config.security.utils.WebMvcWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足异常处理(用来解决认证过的用户访问无权限资源时的异常)
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/15 14:35
 **/
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        log.error("接口-> {} <-访问权限不足，请求失败: {}", request.getRequestURI(), e);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ApiResponse resp = ApiResponse.failed(HttpStatus.FORBIDDEN.value(), "权限不足!!");
        new WebMvcWriter().out(response, resp);
    }
}