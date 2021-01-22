package com.base.web.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * 自定义权限判断
 * 注解使用方式(@PreAuthorize("hasPermission('user','add')" ))
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/13 18:52
 **/
@Configuration
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object o, Object o1) {
        boolean access = false;
        log.info(auth.getPrincipal().toString());
        // 权限判断
        if (auth.getPrincipal().toString().compareToIgnoreCase("anonymousUser") != 0) {
            String privilege = o + ":" + o1;
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (privilege.equalsIgnoreCase(authority.getAuthority())) {
                    access = true;
                    break;
                }
            }
        }
        return access;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
