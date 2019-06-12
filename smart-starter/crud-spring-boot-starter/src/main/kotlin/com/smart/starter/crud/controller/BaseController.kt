package com.smart.starter.crud.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.smart.common.message.Result
import com.smart.starter.crud.model.BaseModel
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
 * 基础controller
 * @author ming
 * 2019/6/12 上午11:20
 */
open class BaseController<K : BaseService<T>, T : BaseModel> {

    private val PAGE_SIZE = "limit"
    private val OFFSET = "offset"
    private val ROWS = "rows"
    private val TOTAL = "total"
    private val SORT_NAME = "sortName"
    private val SORT_ORDER = "sortOrder"

    /**
     * 关键字字段名
     */
    private val keywordField = "keyword"

    @Autowired
    protected lateinit var service: K

    /**
     * 注册日期转换工具
     * @param binder
     */
//    @InitBinder
//    fun initBinder(binder: WebDataBinder) {
//        binder.registerCustomEditor(Date::class.java, CustomDateEditor())
//    }

    /**
     * 查询列表
     * @param parameters
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    protected fun list(@RequestBody parameters: Map<String, Any?>): Result<Any?> {
        try {
            val page = this.doPage(parameters)
            val wrapper = MybatisUtil.createQueryWrapperFromParameters<T>(parameters, this.getModelType())
            // 添加keyword查询
            val keyword = parameters[keywordField] as String
            if (!StringUtils.isEmpty(keyword)) {
                this.addKeyword(wrapper, keyword)
            }
            val data = this.service.list(wrapper, parameters, page != null)
            if (page != null) {
                val returnData = HashMap<String, Any>(2)
                returnData[ROWS] = data
                returnData[TOTAL] = page.total
                return Result.success(returnData)
            }
            return Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message)
        }

    }

    /**
     * 删除接口
     */
    @RequestMapping("/delete")
    @ResponseBody
    protected fun delete(@RequestBody deleteObject: T): Result<Int?> {
        try {
            return Result.success(this.service.delete(deleteObject))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure("删除单个对象时发生错误", 0)
        }

    }

    /**
     * 查询单个对象
     */
    @RequestMapping("/get")
    @ResponseBody
    protected operator fun get(@RequestBody t: T): Result<T?> {
        try {
            return Result.success(this.service.get(t))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message)
        }

    }

    /**
     * 添加更新
     */
    @RequestMapping("/saveUpdate")
    @ResponseBody
    protected fun saveUpdate(@RequestBody t: T): Result<Any?> {
        try {
            return Result.success(this.service.saveOrUpdate(t))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure("保存更新时发生错误", false)
        }

    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    @ResponseBody
    protected fun save(@RequestBody t: T): Result<Boolean?> {
        try {
            return Result.success(this.service.save(t))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message, false)
        }

    }

    /**
     * 批量保存
     */
    @RequestMapping("/batchSave")
    @ResponseBody
    protected fun batchSave(@RequestBody tList: List<T>): Result<Boolean?> {
        try {
            return Result.success(this.service.saveBatch(tList))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message, false)
        }

    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ResponseBody
    protected fun update(@RequestBody t: T): Result<Boolean?> {
        try {
            return Result.success(this.service.updateById(t))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message, false)
        }

    }

    /**
     * 批量删除
     * @param deleteObjects
     * @return
     */
    @RequestMapping("/batchDelete")
    @ResponseBody
    protected fun batchDelete(@RequestBody deleteObjects: List<T>): Result<Any?> {
        try {
            return Result.success(this.service.batchDelete(deleteObjects))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure("批量删除时发生错误", 0)
        }

    }

    /**
     * 执行分期
     * @param parameterSet 前台参数
     * @return 分页结果
     */
    protected fun doPage(parameterSet: Map<String, Any?>): Page<T>? {
        var page: Page<T>? = null
        if (parameterSet[PAGE_SIZE] != null) {
            val limit = parameterSet[PAGE_SIZE] as Int
            val offset = if (parameterSet[OFFSET] == null) 0 else parameterSet[OFFSET] as Int
            val order = this.analysisOrder(parameterSet)
            if (!StringUtils.isEmpty(order)) {
                PageHelper.orderBy(order)
            }
            page = PageHelper.offsetPage(offset, limit)
        }
        return page
    }

    /**
     * 解析排序字段
     * @param parameters 前台参数
     * @return 排序字段
     */
    protected fun analysisOrder(parameters: Map<String, Any?>): String? {
        val sortName = parameters[SORT_NAME] as String
        if (!StringUtils.isEmpty(sortName)) {
            val sortOrder = parameters[SORT_ORDER] as String
            // 解析数据库字段
            val orderMessage = StringBuilder()

            val clazz = MybatisUtil.getModelClass(this.getModelType())
            val sortList = MybatisUtil.analysisOrder(sortName, sortOrder, clazz)
            if (clazz != null && sortList != null) {
                sortList.forEach { sort ->
                    orderMessage.append(sort.dbName)
                            .append(" ")
                            .append(sort.order)
                            .append(",")
                }
                if (orderMessage.length > 0) {
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
    protected fun getModelType(): Type {
        return (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
    }

    /**
     * 添加关键字查询
     * @param wrapper 查询条件
     */
    private fun addKeyword(wrapper: QueryWrapper<T>, keyword: String) {
        // 获取实体类的实际类型
        val tClass = MybatisUtil.getModelClass(this.getModelType())
        if (tClass != null) {
            val fieldList = Arrays.asList<Field>(*tClass.getFields())
            // 遍历添加liken
            fieldList.forEach { field -> wrapper.like(field.name, keyword) }
        }
    }
}