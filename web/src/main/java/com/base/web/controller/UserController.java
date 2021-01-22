package com.base.web.controller;

import com.base.common.ApiResponse;
import com.base.common.annotation.RequestLimit;
import com.base.common.constant.WebConstant;
import com.base.web.entity.dto.SysUserDTO;
import com.base.web.entity.dto.UserRegistryDTO;
import com.base.web.entity.vo.UserLoginVO;
import com.base.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户模块
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/17
 */
@RestController()
@RequestMapping("user")
@Api(value = "用户管理", tags = "后台用户管理")
@CrossOrigin
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestLimit(count = 1, time = 30000, apiName = "user-registry")
    @PostMapping("registry")
    @ApiOperation(value = "注册", notes = "用户注册")
    public ApiResponse registry(@ModelAttribute @Validated UserRegistryDTO registryDTO) throws Exception {
        return userService.registry(registryDTO);
    }

    @PostMapping("login")
    @ApiOperation(value = "登录", notes = "用户登录(支持用户名/手机号码/邮箱)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query", required = true, defaultValue = "admin"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", required = true, defaultValue = "123456")
    })
    public void login(@RequestParam(name = "username") String username,
                      @RequestParam(name = "password") String password) throws Exception {
        // 实际上这里可以不用写相关的登录逻辑，登录和登出交由security处理
        // return userService.login(username, password);
    }

    @PreAuthorize("hasPermission('user','info')")
    @PostMapping("info")
    @ApiOperation(value = "获取用户信息", notes = "需要认证")
    public ApiResponse<UserLoginVO> userInfo() throws Exception {
        return ApiResponse.successful(userService.userInfo().getData());
    }

//    @PostMapping("disable-or-enable-user")
//    @ApiOperation(value = "禁用/启用用户", notes = "禁用/启用用户(管理员可操作0:禁用，1:启用)")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "int", paramType = "query", required = true),
//            @ApiImplicitParam(name = "operation", value = "密码", dataType = "int", paramType = "query", required = true, defaultValue = "0,1")
//    })
//    @RequireAuth(roleName = {"admin"})
//    public ApiResponse disableOrEnableUser(@RequestParam(name = "userId") Integer userId,
//                                         @RequestParam(name = "operation") Integer operation) throws Exception {
//        return userService.disableOrEnableUser(userId, operation);
//    }


//    @RequestLimit(count = 1, time = 60000)
//    @RequireAuth()
//    @GetMapping("real-name-auth")
//    @ApiOperation(value = "实名认证", notes = "实名认证(已购买100次)")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "name", value = "姓名", dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "idNo", value = "身份证号", dataType = "string", paramType = "query"),
//    })
//    public ApiResponse realNameAuth(@RequestParam(name = "name") String name,
//                                  @RequestParam(name = "idNo") String idNo) throws Exception {
//        return userService.realNameAuth(name, idNo);
//    }

    @PostMapping("logout")
    @ApiOperation(value = "登出", notes = "用户登出")
    @ResponseStatus(HttpStatus.OK)
    public void logout() throws Exception {
        // return userService.logout();
    }

//    @PostMapping("password")
//    @ApiOperation(value = "根据旧密码修改登录密码", notes = "在已登录的情况下，修改密码")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "oldPassword", value = "旧密码", dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "string", paramType = "query"),
//    })
//    public ApiResponse updateLoginPasswordByOldPassword(@RequestParam(name = "oldPassword") String oldPassword,
//                                                        @RequestParam(name = "newPassword") String newPassword) throws Exception {
//        return userService.updateLoginPasswordByOldPassword(oldPassword, newPassword);
//    }

    @PutMapping("info")
    @ApiOperation(value = "修改用户信息", notes = "修改后台用户信息(仅限管理员)")
    @ApiIgnore
    public ApiResponse update(@RequestBody @Validated SysUserDTO userDTO) throws Exception {
        return userService.updateUser(userDTO);
    }


//    @PostMapping("/info")
//    @ApiOperation(value = "新增用户", notes = "新增用户")
//    @ApiIgnore
//    public ApiResponse add(@ModelAttribute @Validated AddUserDTO addUserDTO) throws Exception {
//        return userService.addUser(addUserDTO);
//    }
//
//    @DeleteMapping("info/{id}")
//    @ApiOperation(value = "删除用户", notes = "删除用户")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "int", paramType = "path")
//    })
//    @ApiIgnore
//    public ApiResponse delete(@PathVariable(name = "id") Integer id) throws Exception {
//        return userService.deleteUser(id);
//    }

    @PreAuthorize("hasPermission('user','list')")
    @GetMapping("list")
    @ApiOperation(value = "用户列表", notes = "后台用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页显示数", dataType = "int", paramType = "query", defaultValue = "10", example = "10", allowableValues = "10,20,40,60"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "telephone", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "string", paramType = "query"),
    })
    public ApiResponse getUserList(@RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Integer current,
                                   @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Integer size,
                                   @RequestParam(name = "userName", required = false, defaultValue = "") String userName,
                                   @RequestParam(name = "telephone", required = false, defaultValue = "") String telephone,
                                   @RequestParam(name = "email", required = false, defaultValue = "") String email) {
        return userService.userList(current, size, userName, telephone, email);
    }

//    @PostMapping("password/modification/{userId}")
//    @ApiOperation(value = "修改密码(仅管理员可操作)", notes = "修改密码(仅管理员可操作)")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "string", paramType = "path"),
//            @ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "string", paramType = "query"),
//    })
//    @ApiIgnore
//    public ApiResponse changePwd(@PathVariable(name = "userId") String userId,
//                               @RequestParam(name = "newPassword") String newPassword) throws Exception {
//        return userService.changePwd(userId, newPassword);
//    }

//    @RequiresPermissions("user:role:get")
//    @GetMapping("role/{userId}")
//    @ApiOperation(value = "获取用户的角色", notes = "获取用户的角色")
//    public ApiResponse getRole(@PathVariable(name = "userId") Integer userId) {
//        return ApiResponse.successOf(userService.getUserRole(userId));
//    }

//    @RequiresPermissions("user:role:update")
//    @PostMapping("role/{userId}")
//    @ApiOperation(value = "设置用户角色(不保留原来的角色)", notes = "设置用户角色(不保留原来的角色)")
//    @SysLog(value = "设置用户角色(不保留原来的角色)", type = 1)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "int", paramType = "path", required = true),
//            @ApiImplicitParam(name = "roles", value = "角色ids", dataType = "int", paramType = "query", required = true, allowMultiple = true)
//    })
//    @ApiIgnore
//    public ApiResponse updateUserRole(@PathVariable(name = "userId") Integer userId,
//                                    @RequestParam(name = "roles") Integer[] roles) throws Exception {
//        return userService.updateUserRole(userId, roles);
//    }

//    @RequiresPermissions("user:password:forgot")
//    @SysLog(value = "忘记密码")
//    @PostMapping("password/{actionId}")
//    @ApiOperation(value = "根据手机验证码修改登录密码", notes = "可以在未登录的情况下操作")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "actionId", value = "行为ID", dataType = "int", paramType = "path", defaultValue = "1", allowableValues = "1,2,3,4,5,6,7"),
//            @ApiImplicitParam(name = "telephone", value = "手机号码", dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "code", value = "短信验证码", dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "password", value = "新密码", dataType = "string", paramType = "query"),
//    })
//    @ApiIgnore
//    public ApiResponse updateLoginPasswordByTelephone(
//            @PathVariable(name = "actionId") Integer actionId,
//            @RequestParam(name = "telephone") String telephone,
//            @RequestParam(name = "code") String code,
//            @RequestParam(name = "password") String password) throws Exception {
//        return userService.updateLoginPasswordByTelephone(telephone, code, password, actionId);
//    }

}
