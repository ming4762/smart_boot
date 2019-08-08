package com.smart.starter.crud.controller

import com.smart.common.message.Result
import com.smart.starter.crud.model.BaseModel
import com.smart.starter.crud.service.BaseService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

/**
 * 基础controller
 * @author ming
 * 2019/6/12 上午11:20
 */
open class BaseController<K : BaseService<T>, T : BaseModel> : BaseListParameterController<K, T>() {

    /**
     * 删除接口
     */
    @RequestMapping("/delete")
    @ResponseBody
    protected open fun delete(@RequestBody deleteObject: T): Result<Int?> {
        return try {
            Result.success(this.service.batchDelete(listOf(deleteObject)))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure("删除单个对象时发生错误", 0)
        }

    }

    /**
     * 添加更新
     */
    @RequestMapping("/saveUpdate")
    @ResponseBody
    protected open fun saveUpdate(@RequestBody t: T): Result<Any?> {
        return try {
            Result.success(this.service.saveOrUpdate(t))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure("保存更新时发生错误", false)
        }

    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    @ResponseBody
    protected open fun save(@RequestBody t: T): Result<Boolean?> {
        return try {
            Result.success(this.service.save(t))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message, false)
        }

    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ResponseBody
    protected open fun update(@RequestBody t: T): Result<Boolean?> = try {
        Result.success(this.service.updateById(t))
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e.message, false)
    }
}