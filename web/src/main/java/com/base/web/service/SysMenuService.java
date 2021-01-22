package com.base.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.base.common.ApiResponse;
import com.base.common.entity.TreeNode;
import com.base.common.entity.sys.SysMenu;
import com.base.common.exception.BusinessException;
import com.base.web.entity.dto.SysMenuDTO;
import com.base.web.entity.vo.SysMenuVO;

import java.util.List;
import java.util.Set;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/12 14:51
 **/
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 是否已存在权限文案
     *
     * @param permission 权限
     * @return true or false
     * @author wzh
     * @date 2019/9/18 0018 19:39
     */
    ApiResponse isExistPermission(String permission);

    /**
     * 根据用户名查询其角色下的访问菜单列表
     *
     * @param username 用户名
     * @return list menu
     */
    ApiResponse<Set<SysMenu>> selectByUsername(String username);

    /**
     * 菜单列表
     *
     * @return list
     */
    ApiResponse<Set<TreeNode<SysMenu>>> menuList();

    /**
     * 获取菜单树
     *
     * @param list 菜单列表
     * @return 树形菜单
     */
    Set<TreeNode<SysMenu>> getMenuTree(Set<SysMenu> list);

    /**
     * 添加菜单
     *
     * @param backMenuDTO 菜单dto
     * @return json
     * @throws BusinessException e
     */
    ApiResponse addMenu(SysMenuDTO backMenuDTO) throws BusinessException;

    /**
     * 修改菜单
     *
     * @param backMenuDTO 菜单dto
     * @return json
     * @throws BusinessException e
     */
    ApiResponse updateMenu(SysMenuDTO backMenuDTO) throws BusinessException;

    /**
     * 删除菜单
     *
     * @param mid 菜单id
     * @return json
     * @throws BusinessException e
     */
    ApiResponse deleteMenu(String mid) throws BusinessException;

    /**
     * 获取用户的菜单
     *
     * @param uid 用户id
     * @return list
     */
    ApiResponse<Set<SysMenuVO>> getUserMenu(String uid);
}
