package com.base.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 树形菜单实体类
 *
 * @author Administrator
 */
@Data
public class TreeNode<T> {

    private String mid;

    private String pid;

    private String title;

    private String icon;

    private String component;

    private String path;

    private Integer type;

    private String permission;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;

    @JsonIgnore
    private boolean hasParent = false;

    @JsonIgnore
    private boolean hasChildren = false;

    public List<TreeNode<T>> children;

    public void initChildren() {
        this.children = new ArrayList<>();
    }

}
