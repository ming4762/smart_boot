package com.gc.common.base.utils;

import com.gc.common.base.model.Tree;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * 树形工具类
 * @author shizhongming
 * 2020/1/8 9:02 下午
 */
public final class TreeUtils {

    @Nullable
    public static <T> Tree<T> build(@Nullable List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        final List<Tree<T>> topNodes = Lists.newArrayList();
        for (Tree<T> children : nodes) {
            final Serializable pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
                topNodes.add(children);
                continue;
            }
            for (Tree<T> parent : nodes) {
                final Serializable id = parent.getId();
                if (id != null && id == pid) {
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setHasChildren(true);
                }
            }
        }
        Tree<T> root = new Tree<T>();
        if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setId(-1);
            root.setParentId("");
            root.setHasParent(false);
            root.setHasChildren(true);
            root.setChildren(topNodes);
            root.setText("顶级节点");
        }
        return root;
    }

    /**
     * 构建树形列表
     * @param nodes 树形数据
     * @param idParam 上级ID
     * @param <T>
     * @return
     */
    @NotNull
    public static <T> List<Tree<T>>  buildList(@Nullable List<Tree<T>> nodes, @NotNull Serializable idParam) {
        if (nodes == null) {
            return Lists.newArrayList();
        }

        final List<Tree<T>> topNodes = Lists.newArrayList();
        for (Tree<T> children : nodes) {
            final Serializable pid = children.getParentId();
            if (pid == null || idParam == pid) {
                topNodes.add(children);
                continue;
            }
            for (Tree<T> parent : nodes) {
                Serializable id = parent.getId();
                if (id != null && id == pid) {
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setHasChildren(true);
                }
            }
        }
        return topNodes;
    }
}
