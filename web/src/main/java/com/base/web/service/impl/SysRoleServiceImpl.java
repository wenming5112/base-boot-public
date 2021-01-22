package com.base.web.service.impl;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.common.ApiResponse;
import com.base.common.entity.TreeNode;
import com.base.common.entity.sys.SysRole;
import com.base.common.entity.sys.SysRoleMenu;
import com.base.common.enumeration.ResponseMessageEnum;
import com.base.common.exception.BusinessException;
import com.base.common.utils.TreeUtil;
import com.base.web.dao.SysRoleMapper;
import com.base.web.dao.SysRoleMenuMapper;
import com.base.web.entity.dto.SysRoleDTO;
import com.base.web.entity.vo.SysMenuVO;
import com.base.web.entity.vo.SysRoleVO;
import com.base.web.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author ming
 * @date 2019:08:16 16:20
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public ApiResponse<List<SysRole>> selectByUsername(String username) {
        return ApiResponse.successful(roleMapper.selectByUsername(username));
    }

    @Override
    public ApiResponse<List<SysRole>> queryRolesByMenu(String mid) {
        return ApiResponse.successful(roleMapper.queryRolesByMenu(mid));
    }

    /**
     * 获取角色菜单
     *
     * @param rid 角色id
     * @return ApiResponse
     */
    @Override
    public ApiResponse getRoleMenu(String rid) {
        Set<SysMenuVO> list = new HashSet<>(roleMapper.getRoleMenu(rid));
        Set<TreeNode<SysMenuVO>> treeNodeList = new HashSet<>();
        list.forEach(item -> {
            TreeNode<SysMenuVO> treeNode = new TreeNode<>();
            treeNode.setMid(item.getMenuId());
            treeNode.setTitle(item.getTitle());
            treeNode.setComponent(item.getComponent());
            treeNode.setPath(item.getPath());
            treeNode.setType(item.getType());
            treeNode.setPid(item.getPid());
            treeNode.setPermission(item.getPermission());
            treeNode.setDescription(item.getDescription());
            treeNode.setCreateTime(item.getCreateTime());
            treeNodeList.add(treeNode);
        });
        Set<TreeNode<SysMenuVO>> resultList = TreeUtil.build(treeNodeList);
        return ApiResponse.successful(resultList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<Boolean> setRoleMenu(String rid, String[] menus) throws BusinessException {
        try {
            SysRole role = roleMapper.selectById(rid);
            if (ObjectUtils.isEmpty(role)) {
                throw new BusinessException("角色不存在!!");
            }
            // 删除所有的菜单
            SysRoleMenu backstageRoleMenu = new SysRoleMenu();
            backstageRoleMenu.setRid(rid);
            backstageRoleMenu.setValid(true);
            int i = roleMenuMapper.delete(new QueryWrapper<>(backstageRoleMenu));
            if (menus.length == 0) {
                return ApiResponse.successful(i > 0);
            }
            // 在添加
            List<String> newList = new ArrayList<>(Arrays.asList(menus));
//            // db
//            Integer[] menuIds = roleMapper.getRoleMenuIds(roleId);
//            List<Integer> newList = IntegerUtil.listToRepeat(Arrays.asList(menuIds), Arrays.asList(menus));
//            // 筛选完之后 如果集合为空  则不添加 直接返回成功
//            if (newList.size() == 0) {
//                return true;
//            }
//            String operator = UserUtil.getUsername();
//            FabricAssert.judge(roleMapper.selectById(roleId) != null, "角色不存在");
            return ApiResponse.successful(roleMapper.setRoleMenu(rid, newList, "测试用户") > 0);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    @Override
    public ApiResponse roleList(Integer current, Integer size) {
        Page<SysRole> page = baseMapper.selectPage(new Page<>(), new QueryWrapper<SysRole>().lambda().eq(SysRole::getValid, true));
        List<SysRoleVO> backRoleVoList = new ArrayList<>();
        page.getRecords().forEach(item -> {
            SysRoleVO backRoleVO = new SysRoleVO();
            backRoleVO.setRid(item.getId());
            backRoleVO.setRoleName(item.getRoleName());
            backRoleVO.setRoleDesc(item.getRoleDesc());
            backRoleVO.setCreateTime(item.getCreateTime());
            backRoleVO.setModifyTime(item.getModifyTime());
            backRoleVoList.add(backRoleVO);
        });

        Page<SysRoleVO> page1 = new Page<>();
        page1.setRecords(backRoleVoList);
        page1.setTotal(page.getTotal());
        page1.setCurrent(page.getCurrent());
        page1.setOrders(page.getOrders());
        page1.setPages(page.getPages());
        page1.setSize(page.getSize());

        return ApiResponse.successful(page1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse addRole(SysRoleDTO roleDTO) throws BusinessException {
        SysRole role = dtoToEntity(roleDTO);
        //role.setCreator(JwtUtil.getUserNameFromRedis());
        role.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        return this.save(role)
                ? ApiResponse.successful(ResponseMessageEnum.CREATE_SUCCESS.getCode(), ResponseMessageEnum.CREATE_SUCCESS.getMsg())
                : ApiResponse.failed(ResponseMessageEnum.CREATE_FAILED.getCode(), ResponseMessageEnum.CREATE_FAILED.getMsg());
    }

    @Override
    public ApiResponse deleteRole(String rid) throws BusinessException {
        SysRole role = new SysRole();
        role.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//        role.setModifier(JwtUtil.getUserNameFromRedis());
        role.setId(rid);
        role.setValid(false);
        return this.updateById(role)
                ? ApiResponse.successful(ResponseMessageEnum.DELETE_SUCCESS.getCode(), ResponseMessageEnum.DELETE_SUCCESS.getMsg())
                : ApiResponse.failed(ResponseMessageEnum.DELETE_FAILED.getCode(), ResponseMessageEnum.DELETE_FAILED.getMsg());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse updateRole(SysRoleDTO backRoleDTO) throws BusinessException {
        SysRole role = dtoToEntity(backRoleDTO);
//        role.setModifier(JwtUtil.getUserNameFromRedis());
        role.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        return this.updateById(role)
                ? ApiResponse.successful(ResponseMessageEnum.UPDATE_SUCCESS.getCode(), ResponseMessageEnum.UPDATE_SUCCESS.getMsg())
                : ApiResponse.failed(ResponseMessageEnum.UPDATE_FAILED.getCode(), ResponseMessageEnum.UPDATE_FAILED.getMsg());
    }

    private SysRole dtoToEntity(SysRoleDTO dto) {
        SysRole role = new SysRole();
        role.setRoleName(dto.getRoleName());
        role.setId(dto.getRid());
        role.setRoleDesc(dto.getRoleDesc());
        return role;
    }

}
