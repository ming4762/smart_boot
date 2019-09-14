package com.smart.starter.crud.query

import com.smart.starter.crud.model.BaseModel


/**
 * 查询参数类
 * @author ming
 * 2019/9/13 下午5:53
 */
open class QueryParameter<T: BaseModel> : HashMap<String, Any?>() {
    // 关键字查询
    var keyword: String? = null

    override fun toString(): String {
        return "${super.toString()}, keyword=$keyword"
    }


}