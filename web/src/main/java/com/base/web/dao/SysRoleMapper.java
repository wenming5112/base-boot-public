package com.base.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.common.entity.sys.SysRole;
import com.base.web.entity.vo.SysMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 mapper
 *
 * @author ming
 * @date 2020/04/16
 */

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户名查询菜单列表
     *
     * @param username 用户名
     * @return list
     */
    List<SysRole> selectByUsername(@Param("username") String username);

    /**
     * 根据菜单查询可访问的角色
     *
     * @param mid 菜单id
     * @return 角色列表
     */
    List<SysRole> queryRolesByMenu(@Param("mid") String mid);

    /**
     * 查询角色菜单
     *
     * @param rid 角色id
     * @return list
     */
    List<SysMenuVO> getRoleMenu(@Param("rid") String rid);

    /**
     * 设置角色菜单
     *
     * @param rid      角色id
     * @param menus    菜单id列表
     * @param operator 操作人
     * @return int
     */
    int setRoleMenu(@Param("rid") String rid, @Param("menus") List<String> menus, @Param("operator") String operator);

    /**
     * 获取角色已拥有的菜单
     *
     * @param rid 角色id
     * @return list
     */
    String[] getRoleMenuIds(@Param("rid") String rid);

}
