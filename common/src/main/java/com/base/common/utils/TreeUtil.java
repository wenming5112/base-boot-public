package com.base.common.utils;


import com.base.common.entity.TreeNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 构造树形菜单
 *
 * @author wzh
 * @date 2019-03-15 08:45
 **/
public class TreeUtil {

    public static <T> Set<TreeNode<T>> build(Set<TreeNode<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        Set<TreeNode<T>> topNodes = new HashSet<>();
        nodes.forEach(node -> {
            String pid = node.getPid();
            if ("0".equals(pid)) {
                topNodes.add(node);
                return;
            }
            for (TreeNode<T> n : nodes) {
                String id = n.getMid();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty()) {
                topNodes.add(node);
            }
        });
        return topNodes;
    }
}
