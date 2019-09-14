package com.smart.starter.crud.query

import com.smart.starter.crud.model.BaseModel


/**
 *
 * @author ming
 * 2019/9/13 下午6:06
 */
open class SortQueryParameter<T: BaseModel> : QueryParameter<T>() {
    // 排序字段
    var sortName: String? = null
    // 排序方向
    var sortOrder: String? = null

    override fun toString(): String {
        return "${super.toString()}, sortName=$sortName, sortOrder=$sortOrder"
    }


}