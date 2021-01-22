package com.base.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/14 10:12
 **/
@RestController
@RequestMapping("per")
@Slf4j
public class PermissionController {

    /**
     * 密码模式:
     * http://localhost:8080/oauth/oauth/token?username=admin&password=123&grant_type=password&client_id=demoApp&client_secret=demoAppSecret
     * <p>
     * 客户端模式:
     * http://localhost:8080/oauth/oauth/token?grant_type=client_credentials&client_id=demoApp&client_secret=demoAppSecret
     */
    @GetMapping
    public String get() {
        log.info("访问成功");
        return "success";
    }
}
