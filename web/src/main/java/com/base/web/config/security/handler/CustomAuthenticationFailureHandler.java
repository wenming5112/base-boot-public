package com.base.web.config.security.handler;

import com.base.common.ApiResponse;
import com.base.web.config.security.utils.WebMvcWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/21 14:37
 **/
@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.error("登录认证失败: {}", e);
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ApiResponse resp;
        if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
            resp = ApiResponse.failed(1111, "账户名或者密码输入错误!");
        } else if (e instanceof LockedException) {
            resp = ApiResponse.failed(1112, "账户被锁定，请联系管理员!");
        } else if (e instanceof CredentialsExpiredException) {
            resp = ApiResponse.failed(1113, "密码过期，请联系管理员!");
        } else if (e instanceof AccountExpiredException) {
            resp = ApiResponse.failed(1114, "账户过期，请联系管理员!");
        } else if (e instanceof DisabledException) {
            resp = ApiResponse.failed(1115, "账户被禁用，请联系管理员!");
        } else {
            resp = ApiResponse.failed(1116, "登录失败!");
        }
        new WebMvcWriter().out(response, resp);
    }
}
