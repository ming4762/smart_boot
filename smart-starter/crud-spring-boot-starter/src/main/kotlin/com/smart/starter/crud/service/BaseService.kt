package com.smart.starter.crud.service

import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.smart.starter.crud.model.BaseModel

/**
 *
 * @author ming
 * 2019/6/12 上午10:40
 */
interface BaseService<T: BaseModel> : IService<T> {

    /**
     * 删除操作
     */
    fun delete(t: T): Int

    /**
     * 批量删除
     */
    fun batchDelete(tList: List<T>): Int

    /**
     * 查询单个对象
     */
    fun get(t: T): T?

    /**
     * 查询方法
     */
    fun list(queryWrapper: Wrapper<T>, parameters: Map<String, Any?>, paging: Boolean = false): List<T>
}