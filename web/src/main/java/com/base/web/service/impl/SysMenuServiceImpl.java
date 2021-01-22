package com.base.web.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.common.ApiResponse;
import com.base.common.entity.TreeNode;
import com.base.common.entity.sys.SysMenu;
import com.base.common.enumeration.ResponseMessageEnum;
import com.base.common.exception.BusinessException;
import com.base.common.utils.TreeUtil;
import com.base.web.dao.SysMenuMapper;
import com.base.web.entity.dto.SysMenuDTO;
import com.base.web.entity.vo.SysMenuVO;
import com.base.web.service.SysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author ming
 * @date 2019:08:16 16:21
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper menuMapper;

    @Override
    public ApiResponse<Boolean> isExistPermission(String permission) {
        if (StringUtils.isNotBlank(permission)) {
            SysMenu menu = new SysMenu();
            menu.setPermission(permission);
            menu.setValid(true);
            return ApiResponse.successful(menuMapper.selectCount(new QueryWrapper<>(menu)) > 0);
        }
        return ApiResponse.successful(false);
    }

    @Override
    public ApiResponse<Set<SysMenu>> selectByUsername(String username) {
        return ApiResponse.successful(menuMapper.selectByUsername(username));
    }

    @Override
    public ApiResponse<Set<TreeNode<SysMenu>>> menuList() {
        SysMenu menu = new SysMenu();
        menu.setValid(true);
        List<SysMenu> list = menuMapper.selectList(new QueryWrapper<>(menu));
        return ApiResponse.successful(getMenuTree(new HashSet<>(list)));
    }

//    private Set<SysMenu> entity2Vo(Set<SysMenu> list) {
//        Set<SysMenu> vos = new HashSet<>();
//        list.forEach(item -> {
//            SysMenu menuVO = new SysMenu();
//            menuVO.setComponent(item.getComponent())
//                    .(item.getId())
//                    .setIcon(item.getIcon())
//                    .setPath(item.getPath())
//                    .setDescription(item.getDescription())
//                    .setPermission(item.getPermission())
//                    .setPid(item.getParentId())
//                    .setTitle(item.getTitle())
//                    .setType(item.getType())
//            ;
//            vos.add(menuVO);
//        });
//        return vos;
//    }

//    @Override
//    public List<TreeNode<SysMenuVO>> getMenuTree(List<SysMenu> list) {
//        List<TreeNode<SysMenuVO>> treeNodeList = new ArrayList<>();
//        list.forEach(item -> {
//            TreeNode<SysMenuVO> treeNode = new TreeNode<>();
//            treeNode.setMenuId(item.getId());
//            treeNode.setUrl(item.getUrl());
//            treeNode.setIcon(item.getIcon());
//            treeNode.setTitle(item.getTitle());
//            treeNode.setComponent(item.getComponent());
//            treeNode.setPath(item.getPath());
//            treeNode.setType(item.getType());
//            treeNode.setPid(item.getParentId());
//            treeNode.setPermission(item.getPermission());
//            treeNode.setDescription(item.getDescription());
//            treeNode.setCreateTime(item.getCreateTime());
//            treeNodeList.add(treeNode);
//        });
//        return TreeUtil.build(treeNodeList);
//    }

    @Override
    public Set<TreeNode<SysMenu>> getMenuTree(Set<SysMenu> list) {
        Set<TreeNode<SysMenu>> treeNodeList = new HashSet<>();
        list.forEach(item -> {
            TreeNode<SysMenu> treeNode = new TreeNode<>();
            treeNode.setMid(item.getId());
            treeNode.setIcon(item.getIcon());
            treeNode.setTitle(item.getTitle());
            treeNode.setComponent(item.getComponent());
            treeNode.setPath(item.getPath());
            treeNode.setType(item.getType());
            treeNode.setPid(item.getParentId());
            treeNode.setPermission(item.getPermission());
            treeNode.setDescription(item.getDescription());
            treeNode.setCreateTime(item.getCreateTime());
            treeNodeList.add(treeNode);
        });
        return TreeUtil.build(treeNodeList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse addMenu(SysMenuDTO backMenuDTO) throws BusinessException {
        SysMenu menu = dtoToEntity(backMenuDTO);
        Assert.isTrue(!ObjectUtils.isEmpty(menu), "参数为空");
        try {
            if (backMenuDTO.getType() == 0) {
                // 如果是菜单 ，权限可以为空
                if (StringUtils.isBlank(backMenuDTO.getPermission())) {
                    throw new BusinessException("按钮权限不能为空!!");
                }
            }
            // 防止添加重复的权限
            if (this.isExistPermission(Objects.requireNonNull(menu).getPermission()).getData()) {
                return ApiResponse.failed(ResponseMessageEnum.OPERATION_SUCCESS.getCode(), menu.getPermission() + "权限permission已存在");
            }
//            menu.setCreator(JwtUtil.getUserNameFromRedis());
            menu.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            return this.save(menu)
                    ? ApiResponse.successful(ResponseMessageEnum.CREATE_SUCCESS.getCode(), ResponseMessageEnum.CREATE_SUCCESS.getMsg())
                    : ApiResponse.failed(ResponseMessageEnum.CREATE_FAILED.getCode(), ResponseMessageEnum.CREATE_FAILED.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse updateMenu(SysMenuDTO backMenuDTO) throws BusinessException {
        Assert.isTrue(!ObjectUtils.isEmpty(backMenuDTO), "参数为空");
        Assert.isTrue(backMenuDTO.getMid() != null, "菜单id不能为空");
        SysMenu dbMenu = menuMapper.selectById(backMenuDTO.getMid());
        Assert.isTrue(!ObjectUtils.isEmpty(dbMenu), "要修改的菜单不存在");
        dbMenu.setType(backMenuDTO.getType());
        dbMenu.setIcon(backMenuDTO.getIcon());
        dbMenu.setComponent(backMenuDTO.getComponent());
        dbMenu.setPath(backMenuDTO.getPath());
        dbMenu.setType(backMenuDTO.getType());
        dbMenu.setTitle(backMenuDTO.getTitle());
        dbMenu.setParentId(backMenuDTO.getPid());
        dbMenu.setPermission(backMenuDTO.getPermission());
        dbMenu.setDescription(backMenuDTO.getDescription());
//        dbMenu.setModifier(JwtUtil.getUserNameFromRedis());
        dbMenu.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        return this.updateById(dbMenu)
                ? ApiResponse.successful(ResponseMessageEnum.UPDATE_SUCCESS.getCode(), ResponseMessageEnum.UPDATE_SUCCESS.getMsg())
                : ApiResponse.failed(ResponseMessageEnum.UPDATE_FAILED.getCode(), ResponseMessageEnum.UPDATE_FAILED.getMsg());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse deleteMenu(String mid) throws BusinessException {
        SysMenu menu = menuMapper.selectById(mid);
        Assert.isTrue(!ObjectUtils.isEmpty(menu), "找不到菜单");
        List<SysMenu> childrenList = menuMapper.selectMenuByPid(menu.getId());
        Assert.isTrue(childrenList.size() == 0, "该菜单还有子节点,不能删除菜单");
        menu.setValid(false);
        menu.setId(mid);
//        menu.setModifier(JwtUtil.getUserNameFromRedis());
        menu.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        return this.updateById(menu)
                ? ApiResponse.successful(ResponseMessageEnum.DELETE_SUCCESS.getCode(), ResponseMessageEnum.DELETE_SUCCESS.getMsg())
                : ApiResponse.failed(ResponseMessageEnum.DELETE_FAILED.getCode(), ResponseMessageEnum.DELETE_FAILED.getMsg());
    }

    @Override
    public ApiResponse<Set<SysMenuVO>> getUserMenu(String uid) {
        Set<SysMenuVO> list = menuMapper.getUserMenu(uid);
        return ApiResponse.successful(list);
    }

    private <T> SysMenu dtoToEntity(T dto) {
        SysMenu backstageMenu = new SysMenu();
        if (dto instanceof SysMenuDTO) {
            SysMenuDTO menuDTO = (SysMenuDTO) dto;
            backstageMenu.setTitle(menuDTO.getTitle());
            backstageMenu.setComponent(menuDTO.getComponent());
            backstageMenu.setPath(menuDTO.getPath());
            backstageMenu.setIcon(menuDTO.getIcon());
            backstageMenu.setType(menuDTO.getType());
            backstageMenu.setPermission(menuDTO.getPermission());
            backstageMenu.setParentId(menuDTO.getPid());
            return backstageMenu;
        } else {
            return null;
        }
    }
}
