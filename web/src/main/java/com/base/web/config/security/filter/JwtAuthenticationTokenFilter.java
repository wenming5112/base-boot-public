package com.base.web.config.security.filter;

import com.base.common.ApiResponse;
import com.base.common.enumeration.ResponseMessageEnum;
import com.base.common.utils.JsonUtils;
import com.base.common.utils.RedisCacheUtil;
import com.base.web.config.security.utils.JwtTokenUtils;
import com.base.web.config.security.utils.WebMvcWriter;
import com.base.web.entity.vo.UserInfoVO;
import com.base.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt Token Effectiveness Filter
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/15 14:35
 **/
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserService userService;

    @Resource
    private RedisCacheUtil redisCacheUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        if (token != null && StringUtils.startsWith(token, JwtTokenUtils.TOKEN_PREFIX)) {
            token = StringUtils.substring(token, JwtTokenUtils.TOKEN_PREFIX.length());
        } else {
            filterChain.doFilter(request, response);
            return;
        }
        String username = JwtTokenUtils.getUsername(token);

        if (username != null && redisCacheUtil.hasKey(token)) {
            //获得redis中用户的token刷新时效
            String userJson = redisCacheUtil.getString(token);
            UserInfoVO userInfoVO = JsonUtils.parseJsonToObj(userJson, UserInfoVO.class);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (ObjectUtils.isNotEmpty(userInfoVO)) {
                // 说明用户存在且未过期
                String jwtToken = JwtTokenUtils.createToken(userDetails, false);
                // TODO: 2021/1/22 这里可能还需要更新登录地点和登录IP
                userInfoVO.setJwtToken(jwtToken);
                // 刷新缓存
                redisCacheUtil.delete(token);
                redisCacheUtil.setString(jwtToken, JsonUtils.parseObjToJson(userInfoVO), JwtTokenUtils.EXPIRATION);

                response.setHeader(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + jwtToken);
            }

        }
        try {
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);

                if (JwtTokenUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception e) {
            log.error("接口访问-> {} <-请求失败,不合法的令牌!!", request.getRequestURI());
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            new WebMvcWriter().out(response, ApiResponse.failed(HttpStatus.UNAUTHORIZED.value(), ResponseMessageEnum.USER_TOKEN_INVALID.getMsg()));
            return;
        }

        filterChain.doFilter(request, response);
    }

}
