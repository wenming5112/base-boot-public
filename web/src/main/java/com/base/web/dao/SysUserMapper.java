package com.base.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.base.common.entity.sys.SysMenu;
import com.base.common.entity.sys.SysRole;
import com.base.common.entity.sys.SysUser;
import com.base.web.entity.vo.SysMenuVO;
import com.base.web.entity.vo.SysRoleVO;
import com.base.web.entity.vo.UserInfoVO;
import com.base.web.entity.vo.UserListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SysUser mapper
 *
 * @author ming
 * @date 2020/04/16
 */

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 设置用户角色
     *
     * @param uid   用户id
     * @param roles 角色id列表
     * @return int
     */
    int setUserRoles(@Param("uid") String uid, @Param("roles") String[] roles);

    /**
     * 获取用户角色列表
     *
     * @param uid 用户id
     * @return list
     */
    List<SysRoleVO> getUserRole(@Param("uid") String uid);

    /**
     * 添加后台用户
     *
     * @param user user entity
     * @return entity
     */
    int addBackUser(SysUser user);

    /**
     * 获取用户的角色id列表
     *
     * @param uid 用户id
     * @return int
     */
    List<String> getUserRoleIds(@Param("uid") String uid);

    /**
     * 查询用户所有角色以及权限
     *
     * @param uid user ID
     * @return lsi
     */
    List<SysRole> selectUserAllRoles(@Param("uid") String uid);

    /**
     * 查询角色菜单
     *
     * @param rid 角色 ID
     * @return BackMenuVO
     */
    SysMenu getRolesMenus(@Param("rid") String rid);

    /**
     * 查询用户信息
     *
     * @param uid 用户ID
     * @return UserInfoVO
     */
    UserInfoVO selectMyUserInfo(@Param("uid") String uid);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return UserInfoVO
     */
    SysUser selectMyUserInfoByUsername(@Param("username") String username);

    /**
     * 根据邮箱账号查询用户信息
     *
     * @param email 邮箱账号
     * @return UserInfoVO
     */
    SysUser selectMyUserInfoByMail(@Param("email") String email);

    /**
     * 根据手机号查询用户信息
     *
     * @param telephone 手机号
     * @return UserInfoVO
     */
    SysUser selectMyUserInfoByTel(@Param("telephone") String telephone);

    /**
     * 查询所有的用户
     *
     * @param page      page
     * @param username  username
     * @param telephone telephone
     * @param email     email
     * @return list
     */
    Page<UserListVO> selectAllUsers(Page page, @Param("username") String username, @Param("telephone") String telephone, @Param("email") String email);
}
