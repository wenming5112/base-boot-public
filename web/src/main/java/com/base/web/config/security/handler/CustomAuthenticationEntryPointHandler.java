package com.base.web.config.security.handler;

import com.base.common.ApiResponse;
import com.base.web.config.security.utils.JwtTokenUtils;
import com.base.web.config.security.utils.WebMvcWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 令牌异常处理
 * (用来解决匿名用户访问无权限资源时的异常)
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/15 14:36
 **/
@Component
@Slf4j
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        String token = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        response.setContentType("application/json;charset=utf-8");
        int status = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(status);
        if (StringUtils.isEmpty(token)) {
            String message = "未设置Token!!";
            log.error("接口访问-> {} <-请求失败: {} , {}", request.getRequestURI(), e, message);
            new WebMvcWriter().out(response, ApiResponse.failed(10000 + status, message));
            return;
        }
        if (StringUtils.startsWith(token, JwtTokenUtils.TOKEN_PREFIX)) {
            token = StringUtils.substring(token, JwtTokenUtils.TOKEN_PREFIX.length());
        }
        String username = JwtTokenUtils.getUsername(token);
        if (StringUtils.isEmpty(username)) {
            String message = "访问令牌不合法";
            log.error("接口访问-> {} <-请求失败: {} , {}", request.getRequestURI(), e, message);
            new WebMvcWriter().out(response, ApiResponse.failed(10000 + status, message));
        }
    }
}