package com.base.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.base.common.ApiResponse;
import com.base.common.entity.sys.SysRole;
import com.base.common.exception.BusinessException;
import com.base.web.entity.dto.SysRoleDTO;

import java.util.List;

/**
 * @author ming
 * @date 2019:08:16 16:09
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户名查询用户的角色列表
     *
     * @param username 用户名
     * @return list role
     */
    ApiResponse<List<SysRole>> selectByUsername(String username);


    /**
     * 根据菜单查询可访问的角色
     *
     * @param mid 菜单id
     * @return list
     */
    ApiResponse<List<SysRole>> queryRolesByMenu(String mid);

    /**
     * 获取角色菜单
     *
     * @param rid 角色id
     * @return json
     */
    ApiResponse getRoleMenu(String rid);

    /**
     * 为角色设置访问菜单
     *
     * @param rid   角色id
     * @param menus 菜单id列表
     * @return json
     * @throws BusinessException e
     */
    ApiResponse<Boolean> setRoleMenu(String rid, String[] menus) throws BusinessException;

    /**
     * 添加角色
     *
     * @param roleDTO 后台角色dto
     * @return json
     * @throws BusinessException e
     */
    ApiResponse addRole(SysRoleDTO roleDTO) throws BusinessException;

    /**
     * 删除角色
     *
     * @param rid 角色id
     * @return json
     * @throws BusinessException e
     */
    ApiResponse deleteRole(String rid) throws BusinessException;

    /**
     * 修改角色
     *
     * @param roleDTO 角色dto
     * @return json
     * @throws BusinessException e
     */
    ApiResponse updateRole(SysRoleDTO roleDTO) throws BusinessException;

    /**
     * 角色列表
     *
     * @param current 当前页码
     * @param size    分页大小
     * @return json
     */
    ApiResponse roleList(Integer current, Integer size);
}
