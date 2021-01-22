package com.base.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.common.entity.sys.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色 mapper
 *
 * @author ming
 * @date 2020/04/16
 */

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
