package com.base.web.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.common.ApiResponse;
import com.base.common.constant.WebConstant;
import com.base.common.entity.sys.SysRole;
import com.base.common.entity.sys.SysUser;
import com.base.common.entity.sys.SysUserRole;
import com.base.common.enumeration.ResponseMessageEnum;
import com.base.common.enumeration.RoleEnum;
import com.base.common.exception.BusinessException;
import com.base.common.handle.RequestHolder;
import com.base.common.utils.IpUtil;
import com.base.common.utils.PatternUtil;
import com.base.common.utils.RedisCacheUtil;
import com.base.web.config.security.utils.JwtTokenUtils;
import com.base.web.dao.SysRoleMapper;
import com.base.web.dao.SysUserMapper;
import com.base.web.dao.SysUserRoleMapper;
import com.base.web.entity.dto.AddUserDTO;
import com.base.web.entity.dto.SysUserDTO;
import com.base.web.entity.dto.UserRegistryDTO;
import com.base.web.entity.vo.SysRoleVO;
import com.base.web.entity.vo.UserInfoVO;
import com.base.web.entity.vo.UserListVO;
import com.base.web.entity.vo.UserLoginVO;
import com.base.web.service.CommonService;
import com.base.web.service.SysMenuService;
import com.base.web.service.UserService;
import com.github.hiwepy.ip2region.spring.boot.IP2regionTemplate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/13 14:07
 **/
@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private CommonService commonService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IP2regionTemplate ip2region;

    @Resource
    private WebConstant webConstant;

    @Resource
    private RedisCacheUtil redisCacheUtil;

    @Resource
    private SysMenuService menuService;

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
    }

    /**
     * 这里是指用户唯一标识(username、tel、mail都可以)
     *
     * @param username u
     * @return UserDetails
     * @throws UsernameNotFoundException e
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = loadUser(username);
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new UsernameNotFoundException("User is not exists!!");
        }
        return sysUser;
    }

    /**
     * 获取用户
     *
     * @param username username
     * @return user
     * @throws BusinessException e
     */
    public SysUser getSysUser(String username) throws BusinessException {
        SysUser sysUser = loadUser(username);
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new BusinessException("User is not exists!!");
        }
        return sysUser;
    }

    /**
     * 加载用户
     *
     * @param username 用户名
     * @return SysUser
     * @throws BusinessException e
     */
    private SysUser loadUser(String username) throws BusinessException {
        SysUser sysUser;
        if (PatternUtil.verifyTel(username)) {
            sysUser = userMapper.selectMyUserInfoByTel(username);
        } else if (PatternUtil.verifyEmail(username)) {
            sysUser = userMapper.selectMyUserInfoByMail(username);
        } else {
            sysUser = userMapper.selectMyUserInfoByUsername(username);
        }
        return sysUser;
    }


    /**
     * 用户注册
     *
     * @param registryDTO 注册信息
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResponse registry(UserRegistryDTO registryDTO) throws BusinessException {

        log.debug(String.format("--> %s 用户正在注册!!", registryDTO.getUsername()));
        if (!commonService.emailVerify(registryDTO.getEmail(), registryDTO.getVerifyCode())) {
            throw new BusinessException("验证码错误!!");
        }

        SysUser user = userMapper.selectOne(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getUsername, registryDTO.getUsername())
                .and(wrapper -> wrapper.eq(SysUser::getValid, true)));
        if (!org.springframework.util.ObjectUtils.isEmpty(user)) {
            throw new BusinessException("用户名已存在!!");
        }

        user = userMapper.selectOne(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getEmail, registryDTO.getUsername())
                .and(wrapper -> wrapper.eq(SysUser::getValid, Boolean.TRUE)));
        if (!org.springframework.util.ObjectUtils.isEmpty(user)) {
            throw new BusinessException("邮箱已被绑定!! 您可以使用邮箱登陆!!");
        }

        user = userMapper.selectOne(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getTelephone, registryDTO.getTelephone())
                .and(wrapper -> wrapper.eq(SysUser::getValid, Boolean.TRUE)));
        if (!org.springframework.util.ObjectUtils.isEmpty(user)) {
            throw new BusinessException("手机号已被绑定!! 您可以使用手机号登陆!!");
        }

        user = dto2Entity(registryDTO);
        if (userMapper.insert(user) > 0) {
            // 设置一个默认的角色普通用户
            String[] tmpRole = {RoleEnum.USER.getRid()};
            rolesCheck(tmpRole);
            user = userMapper.selectOne(new QueryWrapper<SysUser>().lambda()
                    .eq(SysUser::getUsername, registryDTO.getUsername())
                    .and(wrapper -> wrapper.eq(SysUser::getValid, true)));
            if (userMapper.setUserRoles(user.getId(), tmpRole) > 0) {
                return ApiResponse.successful(ResponseMessageEnum.OPERATION_SUCCESS.getCode(), "注册成功!!");
            }
        }
        return ApiResponse.successful(ResponseMessageEnum.OPERATION_FAILED.getCode(), "注册失败!!");
    }

    /**
     * 这里jwt生成的token是可以解析用户信息的
     * 但jwt不自带刷新token. 所以这里采用redis 和 jwt生成的token 做有效刷新存储
     *
     * @param username 用户名
     * @param password 密码
     * @return json
     * @throws BusinessException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse login(String username, String password) throws BusinessException {
        try {
            SysUser user = judgeLoginType(username);
            // verify pass
            if (!ObjectUtils.isEmpty(user) && passwordEncoder.matches(password, user.getPassword())) {
                // refresh jwt token
                // refresh user info
                user.setLoginIp(IpUtil.getIp(new RequestHolder().getRequest()));
                user.setLoginAddress(ip2region.getRegion(user.getLoginIp()));
                user.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));

            } else {
                return ApiResponse.failed(ResponseMessageEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), ResponseMessageEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
            }

            // 修改登录IP和登录地址
            user.setLoginIp(user.getLoginIp());
            user.setLoginAddress(user.getLoginAddress());

            if (userMapper.update(user, new UpdateWrapper<>(user)) < 1) {
                log.error("用户信息更新失败!!");
                ApiResponse.failed(2, "用户信息更新失败!!");
            }

            // 缓存token
            // 验证通过返回用户部分信息 以及 拥有的角色和菜单
            UserInfoVO userVO = userMapper.selectMyUserInfo(user.getId());
            userVO.setMyMenus(userVO.getRoles());
            userVO.setUserRoutes(menuService.getMenuTree(userVO.getMenus()));
            userVO.setModifyTime(user.getModifyTime());
            userVO.setJwtToken(JwtTokenUtils.createToken(user, false));

            return ApiResponse.successful((UserLoginVO) userVO);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ApiResponse<UserInfoVO> userInfo() throws BusinessException {
//        String token = new RequestHolder().getRequest().getHeader(JwtTokenUtils.TOKEN_HEADER);
//        String username = JwtTokenUtils.getUsername(token);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        SysUser user = userMapper.selectMyUserInfoByUsername(auth.getName());
        UserInfoVO userVO = userMapper.selectMyUserInfo(user.getId());
        userVO.setMyMenus(userVO.getRoles());
        userVO.setUserRoutes(menuService.getMenuTree(userVO.getMenus()));
        userVO.setModifyTime(user.getModifyTime());

        return ApiResponse.successful(userVO);
    }

    @Override
    public UserInfoVO userInfo(String username) throws BusinessException {
        SysUser user = userMapper.selectMyUserInfoByUsername(username);
        UserInfoVO userVO = userMapper.selectMyUserInfo(user.getId());
        userVO.setMyMenus(userVO.getRoles());
        userVO.setUserRoutes(menuService.getMenuTree(userVO.getMenus()));
        userVO.setModifyTime(user.getModifyTime());

        return userVO;
    }

    /**
     * 禁用用户
     *
     * @param uid 用户id
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResponse disableOrEnableUser(String uid, Integer operation) throws BusinessException {
        SysUser user = userMapper.selectById(uid);
        if (org.springframework.util.ObjectUtils.isEmpty(user)) {
            throw new BusinessException("User is not exists!!");
        }
//        user.setModifier(JwtUtil.getUserNameFromRedis());
        user.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        //user.setStatusId(operation);
        switch (operation) {
            case 1:
                if (userMapper.updateById(user) > 0) {
                    return ApiResponse.successful("Enable success!!");
                }
                return ApiResponse.successful("Enable failed!!");
            case 0:
                if (userMapper.updateById(user) > 0) {
                    if (redisCacheUtil.hasKey(user.getUsername())) {
                        String token = redisCacheUtil.getString(user.getUsername());
                        redisCacheUtil.delete(token, user.getUsername());
                    }

                    return ApiResponse.successful("Disable success!!");
                }
                return ApiResponse.successful("Disable failed!!");
            default:
                throw new BusinessException(String.format("Unknown operation %d !!", operation));
        }
    }

    @Override
    public ApiResponse logout() throws BusinessException {
        String token = new RequestHolder().getRequest().getHeader(JwtTokenUtils.TOKEN_HEADER);
        String username = JwtTokenUtils.getUsername(token);
        log.debug(String.format("%s 用户正在登出!!", username));
        if (redisCacheUtil.hasKey(token) || redisCacheUtil.hasKey(username)) {
            redisCacheUtil.delete(token);
            redisCacheUtil.delete(username);
        }
        return ApiResponse.successful("退出成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse addUser(AddUserDTO addUserDTO) throws BusinessException {
        String token = new RequestHolder().getRequest().getHeader(JwtTokenUtils.TOKEN_HEADER);
        String username = JwtTokenUtils.getUsername(token);
        SysUser user = new SysUser();
        user.setUsername(addUserDTO.getUsername());
        if (userMapper.selectOne(new QueryWrapper<>(user)) != null) {
            throw new BusinessException(ResponseMessageEnum.USER_ALREADY_EXIST.getMsg());
        }
        SysUser backstageUser = dto2Entity(addUserDTO);
        backstageUser.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        backstageUser.setCreator(username);
        int i = userMapper.addBackUser(backstageUser);
        Assert.isTrue(i > 0, "新增用户失败");
        // 如果没有设置角色，则设置一个默认的角色
        if (org.springframework.util.ObjectUtils.isEmpty(addUserDTO.getRoles())) {
            String[] tmpRole = {RoleEnum.USER.getRid()};
            addUserDTO.setRoles(tmpRole);
        } else {
            // 角色检查
            rolesCheck(addUserDTO.getRoles());
            Set<String> roleSet = new HashSet<>(Arrays.asList(addUserDTO.getRoles()));
            addUserDTO.setRoles(roleSet.toArray(new String[roleSet.size()]));
        }
        // 再设置用户角色
        int n = userMapper.setUserRoles(backstageUser.getId(), addUserDTO.getRoles());
        return n > 0 ? ApiResponse.successful(1, "新增用户成功") : ApiResponse.failed(1, "新增用户失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse deleteUser(String uid) throws BusinessException {
        String token = new RequestHolder().getRequest().getHeader(JwtTokenUtils.TOKEN_HEADER);
        String username = JwtTokenUtils.getUsername(token);
        SysUser backstageUser = this.getById(uid);
        if (org.springframework.util.ObjectUtils.isEmpty(backstageUser)) {
            return ApiResponse.failed(1, "用户不存在，删除失败");
        }
        backstageUser.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        backstageUser.setValid(false);
        backstageUser.setModifier(username);
        return this.updateById(backstageUser) ? ApiResponse.successful(1, "删除成功") : ApiResponse.failed(1, "删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse updateUser(SysUserDTO userDTO) throws Exception {
        String token = new RequestHolder().getRequest().getHeader(JwtTokenUtils.TOKEN_HEADER);
        String username = JwtTokenUtils.getUsername(token);
        SysUser backstageUser = userMapper.selectById(userDTO.getUid());
        if (org.springframework.util.ObjectUtils.isEmpty(backstageUser)) {
            throw new BusinessException("");
        }
        Assert.isTrue(!checkEmailIsExist(userDTO.getEmail()).getData(), "邮箱已存在");
        Assert.isTrue(!checkTelephoneIsExist(userDTO.getTelephone()).getData(), "手机号码已存在");
        backstageUser.setEmail(userDTO.getEmail());
        backstageUser.setTelephone(userDTO.getTelephone());
        backstageUser.setModifier(username);
        backstageUser.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        boolean i = this.updateById(backstageUser);

        // 角色检查
        rolesCheck(userDTO.getRoles());
        Set<String> roleSet = new HashSet<>(Arrays.asList(userDTO.getRoles()));
        String[] rolesX = roleSet.toArray(new String[roleSet.size()]);
        // 删除用户旧的角色
        SysUserRole userRole = new SysUserRole();
        userRole.setUid(backstageUser.getId());
        userRole.setValid(true);
        userRoleMapper.delete(new QueryWrapper<>(userRole));
        // 设置用户角色
        int o = userMapper.setUserRoles(backstageUser.getId(), rolesX);
        return (i && o > 0) ? ApiResponse.successful(1, "修改成功") : ApiResponse.failed(1, "修改失败");
    }


    @Override
    public ApiResponse userList(Integer current, Integer size, String userName, String telephone, String email) {
        Page<UserListVO> users = userMapper.selectAllUsers(new Page<>(current, size), userName, telephone, email);
        return ApiResponse.successful(users);
    }

    @Override
    public ApiResponse<Boolean> checkUserIsExist(String username) {
        SysUser backstageUser = new SysUser();
        backstageUser.setUsername(username);
        backstageUser.setValid(true);
        return ApiResponse.successful(userMapper.selectCount(new QueryWrapper<>(backstageUser)) > 0);
    }

    @Override
    public ApiResponse<Boolean> checkEmailIsExist(String email) {
        SysUser user = new SysUser();
        user.setEmail(email);
        user.setValid(true);
        return ApiResponse.successful(userMapper.selectCount(new QueryWrapper<>(user)) > 0);
    }

    @Override
    public ApiResponse<Boolean> checkTelephoneIsExist(String telephone) {
        SysUser user = new SysUser();
        user.setTelephone(telephone);
        user.setValid(true);
        return ApiResponse.successful(userMapper.selectCount(new QueryWrapper<>(user)) > 0);
    }

    @Override
    public ApiResponse<List<SysRoleVO>> getUserRole(String uid) {
        return ApiResponse.successful(userMapper.getUserRole(uid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse updateUserRole(String uid, String[] roles) throws BusinessException {
        Assert.isTrue(!ObjectUtils.isEmpty(this.getById(uid)), ResponseMessageEnum.USER_NOT_EXIST.getMsg());
        // 角色检查
        rolesCheck(roles);
        Set<String> roleSet = new HashSet<>(Arrays.asList(roles));
        String[] rolesX = roleSet.toArray(new String[roleSet.size()]);
        // 删除用户旧的角色
        SysUserRole userRole = new SysUserRole();
        userRole.setUid(uid);
        userRole.setValid(true);
        userRoleMapper.delete(new QueryWrapper<>(userRole));
        // 设置用户角色
        int i = userMapper.setUserRoles(uid, rolesX);
        return i > 0 ? ApiResponse.successful(1, "角色设置成功") : ApiResponse.failed(1, "角色设置失败");
    }

    private void rolesCheck(String[] roles) throws BusinessException {
        if (org.springframework.util.ObjectUtils.isEmpty(roles)) {
            throw new BusinessException("角色列表为空");
        }
        // 角色去重
        Set<String> roleSet = new HashSet<>(Arrays.asList(roles));
        List<String> roles2 = new ArrayList<>(roleSet);
        // 先检查角色是否存在
        SysRole role = new SysRole();
        role.setValid(true);
        List<SysRole> roleList = roleMapper.selectList(new QueryWrapper<>(role));
        List<String> roles1 = new ArrayList<>();
        for (SysRole aRoleList : roleList) {
            roles1.add(aRoleList.getId());
        }
        List<String> reduce = roles2.stream().filter(item -> !roles1.contains(item)).collect(toList());
        log.debug("---差集 reduce (list2 - list1)---");
        if (reduce.size() > 0) {
            StringBuilder re = new StringBuilder();
            re.append("角色ID为： ");
            for (String id : reduce) {
                re.append(id).append(", ");
            }
            re.append("角色不存在");
            throw new BusinessException(re.toString());
        }
    }

//    /**
//     * 忘记登录密码，通过手机号码修改密码
//     *
//     * @param telephone   手机号码
//     * @param code        短信验证
//     * @param newPassword 新密码
//     * @return json
//     */
//    @Override
//    public ApiResponse updateLoginPasswordByTelephone(String telephone, String code, String newPassword, Integer actionId) throws BusinessException {
//        try {
//            Assert.isTrue((Boolean) commonService.verifySmsVerificationCode(telephone, code, actionId).getData(),
//                    "短信验证码不正确");
//            SysUser backstageUser = new SysUser();
//            backstageUser.setTelephone(telephone);
//            backstageUser = userMapper.selectOne(new QueryWrapper<>(backstageUser));
//            Assert.isTrue(!ObjectUtils.isEmpty(backstageUser), "密码修改失败");
//            backstageUser.setPassword(DigestAlgorithmUtils.md5Sign(newPassword));
//            backstageUser.setModifier(backstageUser.getUserName());
//            backstageUser.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//            if (userMapper.updateById(backstageUser) > 0) {
//                return ApiResponse.successful("密码修改成功");
//            } else {
//                return ApiResponse.successful("密码修改失败");
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new BusinessException(e);
//        }
//    }

//    @Override
//    public ApiResponse updateLoginPasswordByOldPassword(String oldPassword, String newPassword) throws Exception {
//        UserInfoVO userInfo = JwtUtil.getUserFromRedis();
//        SysUser backstageUser = userMapper.selectById(userInfo.getUid());
//        Assert.isTrue(!org.springframework.util.ObjectUtils.isEmpty(backstageUser), ResponseResultEnum.USER_TOKEN_INVALID.getMsg());
//        if (DigestAlgorithmUtils.md5Verify(oldPassword, backstageUser.getPassword())) {
//            backstageUser.setPassword(DigestAlgorithmUtils.md5Sign(newPassword));
//            backstageUser.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//            backstageUser.setModifier(userInfo.getUserName());
//            if (userMapper.updateById(backstageUser) > 0) {
//                // TODO: 2020/11/26 修改ca用户密码
//                fabricClientManager.getCaManager().update(fabricClientManager.caEnroll(backstageUser.getUserName(), backstageUser.getPassword()), DigestAlgorithmUtils.md5Sign(newPassword));
//                return ApiResponse.successful("密码修改成功");
//            }
//        } else {
//            return ApiResponse.failOf("密码错误");
//        }
//        return ApiResponse.failOf("密码修改失败,稍后再试");
//    }

//    /**
//     * 修改密码(仅管理员可操作)
//     *
//     * @param userId   旧密码
//     * @param password 新密码
//     * @return ApiResponse
//     * @throws BusinessException e
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ApiResponse<String> changePwd(String uid, String password) throws BusinessException {
//        SysUser user = userMapper.selectOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getValid, true)
//                .and(wrapper -> wrapper.eq(SysUser::getId, uid)));
//        if (org.springframework.util.ObjectUtils.isEmpty(user)) {
//            throw new BusinessException("用户不存在");
//        }
//        user.setPassword(DigestAlgorithmUtils.md5Sign(password));
//        int i = userMapper.updateById(user);
//        return i > 0 ? ApiResponse.successful("修改密码成功") : ApiResponse.failOf("修改密码失败");
//    }

    /**
     * dto 转实体
     *
     * @param dto dto
     * @param <T> 泛型
     * @return 实体
     */
    private <T> SysUser dto2Entity(T dto) {
        SysUser user = new SysUser();
        if (dto instanceof SysUserDTO) {
            SysUserDTO backUserDTO = (SysUserDTO) dto;
            user.setId(backUserDTO.getUid());
            user.setEmail(backUserDTO.getEmail());
            user.setTelephone(backUserDTO.getTelephone());
        } else if (dto instanceof AddUserDTO) {
            AddUserDTO addUserDTO = (AddUserDTO) dto;
            user.setUsername(addUserDTO.getUsername());
            user.setPassword(passwordEncoder.encode(addUserDTO.getPassword()));
            user.setEmail(addUserDTO.getEmail());
            user.setTelephone(addUserDTO.getTelephone());
        } else if (dto instanceof UserRegistryDTO) {
            UserRegistryDTO registryDTO = (UserRegistryDTO) dto;
            user.setUsername(registryDTO.getUsername())
                    .setPassword(passwordEncoder.encode(registryDTO.getPassword()))
                    .setTelephone(registryDTO.getTelephone())
                    .setEmail(registryDTO.getEmail())
                    .setCreator(registryDTO.getUsername())
                    .setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        }
        return user;
    }

    private SysUser judgeLoginType(String username) throws BusinessException {
        SysUser user;
        if (Pattern.compile(PatternUtil.TELEPHONE_REG).matcher(username).find()) {
            user = userMapper.selectMyUserInfoByTel(username);
        } else if (Pattern.compile(PatternUtil.EMAIL_REG).matcher(username).find()) {
            user = userMapper.selectMyUserInfoByMail(username);
        } else if (Pattern.compile(PatternUtil.USERNAME_REG).matcher(username).find()) {
            user = userMapper.selectMyUserInfoByUsername(username);
        } else {
            throw new BusinessException("Invalid username!!");
        }
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("User is not exists!!");
        }
        return user;
    }

}
