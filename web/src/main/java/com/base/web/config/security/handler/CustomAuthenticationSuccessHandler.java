package com.base.web.config.security.handler;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.common.ApiResponse;
import com.base.common.entity.sys.SysUser;
import com.base.common.handle.RequestHolder;
import com.base.common.utils.IpUtil;
import com.base.common.utils.JsonUtils;
import com.base.common.utils.RedisCacheUtil;
import com.base.web.config.security.utils.JwtTokenUtils;
import com.base.web.config.security.utils.WebMvcWriter;
import com.base.web.entity.vo.UserInfoVO;
import com.base.web.service.SysMenuService;
import com.base.web.service.UserService;
import com.github.hiwepy.ip2region.spring.boot.IP2regionTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功处理
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/21 14:49
 **/
@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private UserService userService;

    @Resource
    private RedisCacheUtil redisCacheUtil;

    @Resource
    private IP2regionTemplate ip2region;

    @Resource
    private SysMenuService menuService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        String username = auth.getName();
        log.info("用户-> {} <-登录成功", username);
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());

        SysUser user = userService.getSysUser(username);

        try {
            String jwtToken = JwtTokenUtils.createToken(userService.loadUserByUsername(username), false);
            // 生成令牌并设置到响应头中
            // response.setHeader(JwtTokenUtils.TOKEN_HEADER, token);

            // refresh user info
            user.setLoginIp(IpUtil.getIp(new RequestHolder().getRequest()));
            user.setLoginAddress(ip2region.getRegion(user.getLoginIp()));
            user.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));

            if (redisCacheUtil.hasKey(jwtToken)) {
                //获得redis中用户的token刷新时效
                String userJson = redisCacheUtil.getString(jwtToken);
                UserInfoVO userInfoVO = JsonUtils.parseJsonToObj(userJson, UserInfoVO.class);
                UserDetails userDetails = userService.loadUserByUsername(username);
                if (ObjectUtils.isNotEmpty(userInfoVO)) {
                    // 说明用户存在且未过期
                    jwtToken = JwtTokenUtils.createToken(userDetails, false);
                    // TODO: 2021/1/22 这里可能还需要更新登录地点和登录IP
                    userInfoVO.setJwtToken(jwtToken);
                    // 刷新缓存
                    redisCacheUtil.delete(jwtToken);
                    redisCacheUtil.setString(jwtToken, JsonUtils.parseObjToJson(userInfoVO), JwtTokenUtils.EXPIRATION);
                    Map<String, String> map = new HashMap<>(1);
                    map.put(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + jwtToken);
                    new WebMvcWriter().out(response, ApiResponse.successful(map));
                    return;
                }

            }

            // 修改登录IP和登录地址
            user.setLoginIp(user.getLoginIp());
            user.setLoginAddress(user.getLoginAddress());

            if (!userService.update(user, new UpdateWrapper<>(user))) {
                log.error("用户信息更新失败!!");
                ApiResponse.failed(2, "用户信息更新失败!!");
            }

            // 验证通过返回用户部分信息 以及 拥有的角色和菜单
            UserInfoVO userVO = userService.userInfo(user.getUsername());
            userVO.setMyMenus(userVO.getRoles());
            userVO.setUserRoutes(menuService.getMenuTree(userVO.getMenus()));
            userVO.setModifyTime(user.getModifyTime());
            userVO.setJwtToken(jwtToken);
            redisCacheUtil.setString(jwtToken, JsonUtils.parseObjToJson(userVO), JwtTokenUtils.EXPIRATION);

            Map<String, String> map = new HashMap<>(1);
            map.put(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + jwtToken);
            new WebMvcWriter().out(response, ApiResponse.successful(map));
        } catch (Exception e) {
            log.error("登录认证失败: {}", e);
            new WebMvcWriter().out(response, ApiResponse.failed(HttpStatus.UNAUTHORIZED.value(), "创建token失败，请与管理员联系"));
        }
    }
}
