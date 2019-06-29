package com.smart.common.utils

import com.smart.common.model.Tree
import java.util.*

/**
 * 属性数据工具类
 * @author ming
 * 2019/6/12 上午9:55
 */
object TreeUtils {
    /**
     * 构建树形
     */
    @JvmStatic
    fun <T> build(nodes: List<Tree<T>>?): Tree<T>? {
        if (nodes == null) {
            return null
        }
        val topNodes = ArrayList<Tree<T>>()
        for (children in nodes) {
            val pid = children.parentId
            if (pid == null || "0" == pid) {
                topNodes.add(children)
                continue
            }
            for (parent in nodes) {
                val id = parent.id
                if (id != null && id == pid) {
                    parent.children.add(children)
                    children.isHasParent = true
                    parent.setChildren(true)
                    continue
                }
            }
        }
        var root = Tree<T>()
        if (topNodes.size == 1) {
            root = topNodes[0]
        } else {
            root.id = "-1"
            root.parentId = ""
            root.isHasParent = false
            root.setChildren(true)
            root.isChecked = true
            root.children = topNodes
            root.text = "顶级节点"
            val state = HashMap<String, Any>(16)
            state["opened"] = true
            root.state = state
        }
        return root
    }

    /**
     * 构建树形列表
     */
    @JvmStatic
    fun <T> buildList(nodes: List<Tree<T>>?, idParam: String): List<Tree<T>> {
        if (nodes == null) {
            return emptyList()
        }
        val topNodes = ArrayList<Tree<T>>()
        for (children in nodes) {
            val pid = children.parentId
            if (pid == null || idParam == pid) {
                topNodes.add(children)
                continue
            }
            for (parent in nodes) {
                val id = parent.id
                if (id != null && id == pid) {
                    parent.children.add(children)
                    children.isHasParent = true
                    parent.setChildren(true)
                    continue
                }
            }
        }
        return topNodes
    }
}