package com.base.web.config.security.handler;

import com.base.common.ApiResponse;
import com.base.common.entity.sys.SysUser;
import com.base.common.enumeration.ResponseMessageEnum;
import com.base.common.exception.BusinessException;
import com.base.common.utils.RedisCacheUtil;
import com.base.web.config.security.utils.JwtTokenUtils;
import com.base.web.config.security.utils.WebMvcWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销登录处理
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/21 14:53
 **/
@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisCacheUtil redisCacheUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        String token = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        if (token != null && StringUtils.startsWith(token, JwtTokenUtils.TOKEN_PREFIX)) {
            token = StringUtils.substring(token, JwtTokenUtils.TOKEN_PREFIX.length());
        } else {
            log.error("登出失败，未设置Token");
            throw new BusinessException("登出失败");
        }
        String username = JwtTokenUtils.getUsername(token);
        log.debug(String.format("%s 用户正在登出!!", username));
        if (redisCacheUtil.hasKey(token)) {
            redisCacheUtil.delete(token);
        }
        log.info("用户-> {} <-登出成功", ((SysUser) auth).getUsername());
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        //response.sendRedirect("/login_page");
        new WebMvcWriter().out(response, ApiResponse.successful(ResponseMessageEnum.USER_LOGOUT_SUCCESS.getCode(), ResponseMessageEnum.USER_LOGOUT_SUCCESS.getMsg()));
    }
}
