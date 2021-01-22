package com.base.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.common.entity.sys.SysMenu;
import com.base.web.entity.vo.SysMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单 mapper
 *
 * @author ming
 * @date 2020/04/16
 */

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户名查询用户菜单列表
     *
     * @param username 用户名
     * @return list
     */
    Set<SysMenu> selectByUsername(@Param("username") String username);

    /**
     * 获取用户菜单列表
     *
     * @param uid 用户id
     * @return ist
     */
    Set<SysMenuVO> getUserMenu(@Param("uid") String uid);

    /**
     * 根据父级ID查询菜单
     *
     * @param pid 父级ID
     * @return list
     */
    List<SysMenu> selectMenuByPid(@Param("pid") String pid);

}
