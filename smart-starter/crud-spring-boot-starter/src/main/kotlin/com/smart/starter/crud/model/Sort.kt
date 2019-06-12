package com.smart.starter.crud.model

/**
 * 排序字段
 * @author ming
 * 2019/6/12 上午10:42
 */
class Sort(var name: String, order: String?) {

    var order: String = order ?: "asc"

    var dbName: String? = null
}