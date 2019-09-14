package com.smart.starter.crud.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.smart.common.message.Result
import com.smart.starter.crud.model.BaseModel
import com.smart.starter.crud.model.PageData
import com.smart.starter.crud.query.PageQueryParameter
import com.smart.starter.crud.query.SortQueryParameter
import com.smart.starter.crud.service.BaseService
import com.smart.starter.crud.utils.MybatisUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

/**
 * 基础查询功能
 * @author ming
 * 2019/8/7 上午10:52
 */
open class BaseControllerQuery<K : BaseService<T>, T : BaseModel> {

    @Autowired
    protected lateinit var service: K

    /**
     * 查询列表
     * @param parameters
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    protected open fun list(@RequestBody parameter: PageQueryParameter<T>): Result<Any?> {
        try {
            val page = this.doPage(parameter)
            val queryWrapper = MybatisUtil.createQueryWrapperFromParameters(parameter, this.getModelType())
            // 添加keyword查询
            if (!StringUtils.isEmpty(parameter.keyword)) {
                this.addKeyword(queryWrapper, parameter.keyword!!)
            }
            val data = this.service.list(queryWrapper, parameter, page != null)
            if (page != null) {
                return Result.success(PageData(data, page.total))
            }
            return Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message)
        }

    }

    /**
     * 查询单个对象
     */
    @RequestMapping("/get")
    @ResponseBody
    protected open fun get(@RequestBody t: T): Result<T?> {
        return try {
            Result.success(this.service.get(t))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }

    }

    /**
     * 执行分页
     * @param parameter 前台参数
     * @return 分页结果
     */
    protected open fun doPage(parameter:  PageQueryParameter<T>): Page<T>? {
        var page: Page<T>? = null
        if (parameter.limit != null) {
            // 解析排序信息
            val orderMessage = this.analysisOrder(parameter)
            if (!StringUtils.isEmpty(orderMessage)) {
                PageHelper.orderBy(orderMessage)
            }
            page = PageHelper.offsetPage(parameter.offset, parameter.limit!!)
        }
        return page
    }


    /**
     * 解析排序字段
     * @param parameter 前台参数
     * @return 排序字段
     */
    protected open fun analysisOrder(parameter: SortQueryParameter<T>): String? {
        if (!StringUtils.isEmpty(parameter.sortName)) {
            // 获取实体类
            val clazz = MybatisUtil.getModelClassByType(getModelType())
            if (clazz != null) {
                // 解析数据库字段
                val orderMessage = StringBuilder()
                val sortList = MybatisUtil.analysisOrder(parameter.sortName!!, parameter.sortOrder, clazz)
                if (sortList.isEmpty()) return null
                sortList.forEach { sort ->
                    orderMessage.append(sort.dbName)
                            .append(" ")
                            .append(sort.order)
                            .append(",")
                }
                if (orderMessage.isNotEmpty()) {
                    orderMessage.deleteCharAt(orderMessage.length - 1)
                }
                return orderMessage.toString()
            }
        }
        return null
    }

    /**
     * 获取实体类类型
     * @return
     */
    protected open fun getModelType(): Type {
        return (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
    }

    /**
     * 添加关键字查询
     * @param wrapper 查询条件
     */
    private fun addKeyword(wrapper: QueryWrapper<T>, keyword: String) {
        // 获取实体类的实际类型
        val tClass = MybatisUtil.getModelClassByType(this.getModelType())
        if (tClass != null) {
            val fieldList = Arrays.asList<Field>(*tClass.declaredFields)
            if (fieldList.isNotEmpty()) {
                wrapper.and {
                    // 遍历添加liken
                    fieldList.forEach { field -> it.or().like(MybatisUtil.getDbField(field), keyword) }
                }
            }

        }
    }

}