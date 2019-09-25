package com.smart.starter.crud.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.smart.starter.crud.model.BaseModel
import com.smart.starter.crud.query.PageQueryParameter

/**
 *
 * @author ming
 * 2019/6/12 上午10:40
 */
interface BaseService<T: BaseModel> : IService<T> {

    /**
     * 删除操作
     */
    fun delete(model: T): Int

    /**
     * 批量删除
     */
    fun batchDelete(modelList: List<T>): Int

    /**
     * 查询单个对象
     */
    fun get(model: T): T?

    /**
     * 查询详情
     */
    fun queryDetail(t: T): T?

    /**
     * 查询方法
     */
    fun list(queryWrapper: QueryWrapper<T>, parameter: PageQueryParameter, paging: Boolean = false): List<T>
}