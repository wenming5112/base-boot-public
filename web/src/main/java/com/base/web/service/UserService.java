package com.base.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.base.common.ApiResponse;
import com.base.common.entity.sys.SysUser;
import com.base.common.exception.BusinessException;
import com.base.web.entity.dto.AddUserDTO;
import com.base.web.entity.dto.SysUserDTO;
import com.base.web.entity.dto.UserRegistryDTO;
import com.base.web.entity.vo.SysRoleVO;
import com.base.web.entity.vo.UserInfoVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/12 13:45
 **/

public interface UserService extends IService<SysUser>, UserDetailsService {

    /**
     * 获取用户
     *
     * @param username username
     * @return user
     * @throws BusinessException e
     */
    SysUser getSysUser(String username) throws BusinessException;

    /**
     * 用户注册
     *
     * @param registryDTO 注册信息
     * @return res
     * @throws BusinessException e
     */
    ApiResponse registry(UserRegistryDTO registryDTO) throws BusinessException;

    /**
     * 用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return json
     * @throws IOException              e1
     * @throws NoSuchAlgorithmException e2
     * @throws BusinessException        e3
     */
    ApiResponse login(String username, String password) throws IOException, NoSuchAlgorithmException, BusinessException;

    /**
     * 获取用户信息
     *
     * @return UserLoginVO
     * @throws BusinessException e
     */
    ApiResponse<UserInfoVO> userInfo() throws BusinessException;

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return UserInfoVO
     * @throws BusinessException e
     */
    UserInfoVO userInfo(String username) throws BusinessException;

    /**
     * 禁用用户
     *
     * @param uid       用户id
     * @param operation operation(开启或者关闭)
     * @return res
     * @throws BusinessException e
     */
    ApiResponse disableOrEnableUser(String uid, Integer operation) throws BusinessException;

    /**
     * 用户登出
     *
     * @return json
     * @throws BusinessException e
     */
    ApiResponse logout() throws BusinessException;

    /**
     * 添加一个后台用户
     *
     * @param addUserDTO register param
     * @return json
     * @throws BusinessException e
     */
    ApiResponse addUser(AddUserDTO addUserDTO) throws BusinessException;

    /**
     * 删除用户
     *
     * @param uid uid
     * @return ApiResponse
     * @throws BusinessException e
     */
    ApiResponse deleteUser(String uid) throws BusinessException;

    /**
     * 修改用户信息
     *
     * @param userDTO user dto
     * @return json
     * @throws BusinessException e
     */
    ApiResponse updateUser(SysUserDTO userDTO) throws Exception;

    /**
     * 后台用户列表
     *
     * @param current   1
     * @param size      10
     * @param userName  用户名
     * @param telephone 电话号码
     * @param email     邮箱
     * @return json
     */
    ApiResponse userList(Integer current, Integer size, String userName, String telephone, String email);

    /**
     * 获取用户角色
     *
     * @param uid userId
     * @return json
     */
    ApiResponse<List<SysRoleVO>> getUserRole(String uid);

    /**
     * 修改用户角色
     *
     * @param uid   uid
     * @param roles roles
     * @return List<BackRoleVO>
     * @throws BusinessException e
     */
    ApiResponse updateUserRole(String uid, String[] roles) throws BusinessException;

    /**
     * 检测用户是否存在
     *
     * @param username 用户名
     * @return true or false
     * @date 2019/9/7 0007 11:55
     */
    ApiResponse checkUserIsExist(String username);

    /**
     * 邮箱是否已存在
     *
     * @param email 邮箱
     * @return true or false
     * @date 2019/9/18 0018 21:13
     */
    ApiResponse checkEmailIsExist(String email);

    /**
     * 手机号是否已存在
     *
     * @param telephone 手机号码
     * @return true or false
     * @date 2019/9/18 0018 21:13
     */
    ApiResponse checkTelephoneIsExist(String telephone);

//    /**
//     * 忘记登录密码，通过手机号码修改密码
//     *
//     * @param telephone   手机号码
//     * @param code        短信验证
//     * @param newPassword 新密码
//     * @param actionId    行为ID
//     * @return json
//     * @throws BusinessException e
//     */
//    ApiResponse updateLoginPasswordByTelephone(String telephone, String code, String newPassword, Integer actionId) throws BusinessException;

//    /**
//     * 修改密码
//     *
//     * @param oldPassword 旧密码
//     * @param newPassword 新密码
//     * @return ApiResponse
//     * @throws BusinessException e
//     */
//    ApiResponse updateLoginPasswordByOldPassword(String oldPassword, String newPassword) throws Exception;
}
