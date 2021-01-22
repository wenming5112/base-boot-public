package com.base.web.controller;

import com.base.common.ApiResponse;
import com.base.web.config.upload.FileUploadProperties;
import com.base.web.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ming
 * @version 1.0.0
 * @since 2020 11:11
 */
@RequestMapping("common")
@RestController
@Slf4j
@Api(value = "公共接口", tags = "公共接口")
@CrossOrigin
@AllArgsConstructor
public class CommonController {

    private final FileUploadProperties uploadProperties;

    private final MailService mailService;

    @PostMapping("test")
    @ApiOperation(value = "测试")
    public ApiResponse<String> uploadFile() {
        log.debug(uploadProperties.getLocal().getPath());
        log.debug(uploadProperties.getLocal().getUrl());
        log.debug(uploadProperties.getOss().getAliOss().getAccessKey());
        log.debug(uploadProperties.getOss().getAliOss().getAccessSecret());
        return ApiResponse.successful(10010, "操作成功", "1");
    }

    @PostMapping("email")
    @ApiOperation(value = "发送邮件验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiver", value = "接收者(邮箱)", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query", required = true)
    })
    public ApiResponse email(@RequestParam(name = "receiver") String receiver, @RequestParam(name = "username") String username) {
        return mailService.sendForLogin(receiver, username);
    }

}
