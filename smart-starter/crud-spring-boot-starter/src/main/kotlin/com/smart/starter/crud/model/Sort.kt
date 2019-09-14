package com.smart.starter.crud.model

/**
 * 排序字段
 * @author ming
 * 2019/6/12 上午10:42
 */
class Sort(val name: String, val order: String = "asc") {

    // 数据库名称
    lateinit var dbName: String
}