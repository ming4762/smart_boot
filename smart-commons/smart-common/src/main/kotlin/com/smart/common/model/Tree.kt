package com.smart.common.model

import com.alibaba.fastjson.JSON
import java.io.Serializable

/**
 * 树形数据实体类
 * @author ming
 * 2019/6/12 上午9:56
 */
class Tree<T> : Serializable {

    companion object {
        private const val serialVersionUID = 8130562939365681022L
    }

    /**
     * 节点ID
     */
    var id: String? = null
    /**
     * 显示节点文本
     */
    var text: String? = null
    /**
     * 节点状态，open closed
     */
    var state: Map<String, Any>? = null
    /**
     * 节点是否被选中 true false
     */
    var isChecked = false
    /**
     * 节点属性
     */
    var attributes: Map<String, Any>? = null

    var `object`: T? = null

    /**
     * 节点的子节点
     */
    var children: MutableList<Tree<T>> = mutableListOf()

    /**
     * 父ID
     */
    var parentId: String? = null
    /**
     * 是否有父节点
     */
    var isHasParent = false
    /**
     * 是否有子节点
     */
    var isHasChildren = false
        private set

    fun setChildren(isChildren: Boolean) {
        this.isHasChildren = isChildren
    }

    constructor(id: String, text: String, state: Map<String, Any>, checked: Boolean, attributes: Map<String, Any>,
                children: MutableList<Tree<T>>, isParent: Boolean, isChildren: Boolean, parentID: String) : super() {
        this.id = id
        this.text = text
        this.state = state
        this.isChecked = checked
        this.attributes = attributes
        this.children = children
        this.isHasParent = isParent
        this.isHasChildren = isChildren
        this.parentId = parentID
    }

    constructor() : super() {}

    override fun toString(): String {

        return JSON.toJSONString(this)
    }
}