package com.smart.starter.crud.query

import com.smart.starter.crud.model.BaseModel


/**
 * 分页查询参数
 * @author ming
 * 2019/9/13 下午6:00
 */
open class PageQueryParameter<T: BaseModel> : SortQueryParameter<T>() {

    var limit: Int? = null

    var offset: Int = 0

    // 关键字查询
    var keyword: String? = null

    override fun toString(): String {
        return "${super.toString()}, limit=$limit, offset=$offset)"
    }

}