package com.smart.starter.crud.utils

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.smart.starter.crud.model.BaseModel
import com.smart.starter.crud.model.Sort
import org.slf4j.LoggerFactory
import org.springframework.util.StringUtils
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Type
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 *
 * @author ming
 * 2019/6/12 上午10:41
 */
object MybatisUtil {
    private val logger = LoggerFactory.getLogger(MybatisUtil::class.java)

    /**
     * 方法映射
     */
    private val wrapperMethodMap = mapOf<String, Method>(
            "=" to QueryWrapper :: class.java.getMethod("eq", Any::class.java, Any::class.java),
            "like" to QueryWrapper :: class.java.getMethod("like", Any::class.java, Any::class.java),
            ">" to QueryWrapper :: class.java.getMethod("gt", Any::class.java, Any::class.java),
            ">=" to QueryWrapper :: class.java.getMethod("ge", Any::class.java, Any::class.java),
            "<>" to QueryWrapper :: class.java.getMethod("ne", Any::class.java, Any::class.java),
            "<" to QueryWrapper :: class.java.getMethod("lt", Any::class.java, Any::class.java),
            "<=" to QueryWrapper :: class.java.getMethod("le", Any::class.java, Any::class.java),
            "in" to QueryWrapper :: class.java.getMethod("in", Any::class.java, Collection::class.java)
    )

    //存储类型跟keylist对应关系
    private val typeKeyMap = mutableMapOf<Type, List<String>>()
    // 存储类型
    private val classMap: MutableMap<String, Class<*>> = mutableMapOf()
    // 存储字段信息
    private val fieldMap: MutableMap<Field, String> = mutableMapOf()



    /**
     * 获取实体类的ID信息
     */
    @JvmStatic
    fun getkeyList(type: Type): List<String>? {
        // 从缓存中获取
        var keyList = typeKeyMap[type]
        if (keyList == null) {
            // 获取实体类的类型
            val clazz = getModelClass(type)
            if (clazz != null) {
                keyList = getModelKeyField(clazz)
                typeKeyMap[type] = keyList
            }
        }
        return keyList
    }

    /**
     * 获取实体类主键字段
     */
    @JvmStatic
    fun getModelKeyField(clazz: Class<*>): List<String> {
        val keyList = mutableListOf<String>()
        // 获取类所有属性
        val fieldList = clazz.declaredFields
        var idField: String = ""
        fieldList.forEach {
            // 判断字段名是否为"id"
            if (it.name == "id") {
                idField = "id"
            }
            // 获取主键注解
            val tableId = it.getAnnotation(TableId :: class.java)
            if (tableId != null) {
                keyList.add(it.name)
            }
        }
        if (keyList.isEmpty() && idField != "") {
            keyList.add(idField)
        }
        return keyList
    }

    /**
     * 获取实体类的类型
     */
    @JvmStatic
    fun getModelClass(type: Type?): Class<*>? {
        var clazz: Class<*>? = null
        if (type != null) {
            clazz = classMap[type.typeName]
            if (clazz == null) {
                clazz = Class.forName(type.typeName)
                classMap[type.typeName] = clazz
            }
        }
        return clazz
    }

    /**
     * 根据实体类属性获取数据库字段名称
     * 1、优先从注解获取
     * 2、如果没有注解，属性名去除驼峰表示作为数据库字段
     */
    @JvmStatic
    fun getDbField(field: Field): String {
        var dbField = fieldMap[field]
        if (dbField == null) {
            // 获取注解值
            dbField = field.getAnnotation(TableField :: class.java)?.value
            if (StringUtils.isEmpty(dbField)) {
                // 如果未指定数据库字段，通过驼峰表示获取
                dbField = getDefaultDbField(field.name)
            }
            fieldMap[field] = dbField!!
        }

        return dbField
    }

    /**
     * 获取数据库字段
     */
    @JvmStatic
    fun getDbField(clazz: Class<*>, fieldName: String): String {
        val field = clazz.getDeclaredField(fieldName)
        return this.getDbField(field)
    }

    /**
     * 获取数据库字段
     */
    @JvmStatic
    fun getDbField(property: KMutableProperty1<out BaseModel, *>): String? {
        var dbField = property.javaField?.getAnnotation(TableField :: class.java)?.value
        if (StringUtils.isEmpty(dbField)) {
            val fieldName = property.javaField?.name
            if (fieldName != null) {
                dbField = getDefaultDbField(fieldName)
            }
        }
        return dbField
    }

    /**
     * 获取数据库字段值
     */
    @JvmStatic
    fun getDefaultDbField(fieldName: String): String {
        return com.smart.common.utils.StringUtils.underscoreName(fieldName)
    }

    /**
     * 从参数中创建查询条件
     */
    @JvmStatic
    fun <T> createQueryWrapperFromParameters(paramters: Map<String, Any?>, type: Type?): QueryWrapper<T> {
        val queryWrapper = QueryWrapper<T>()
        val clazz = this.getModelClass(type) as Class<T>?
        if (clazz != null) {
            return this.createQueryWrapperFromParameters(paramters, clazz)
        }
        return queryWrapper
    }

    /**
     * 从参数中创建查询条件
     */
    @JvmStatic
    fun <T> createQueryWrapperFromParameters(paramters: Map<String, Any?>, clazz: Class<T>): QueryWrapper<T> {
        val queryWrapper = QueryWrapper<T>()
        paramters.forEach { key, value ->
            if (key.contains("@")) {
                val keySplit = key.split("@")
                val symble = if (keySplit.size > 1) keySplit[1] else null
                if (value != null) {
                    if (value != "") {
                        wrapperMethodMap[symble]?.invoke(queryWrapper, this.getDbField(clazz, keySplit[0]), value)
                    }
                } else {
                    // 处理null
                    if (symble == "=") {
                        queryWrapper.isNull(this.getDbField(clazz, keySplit[0]))
                    } else if (symble == "<>") {
                        queryWrapper.isNotNull(this.getDbField(clazz, keySplit[0]))
                    } else {
                        logger.warn("null值参数只能使用'='或'<>'")
                    }
                }
            }
        }
        return queryWrapper
    }

    /**
     * 获取数据库字段
     */
    @JvmStatic
    fun getDbFieldList(clazz: Class<*>, fieldNameList: List<String>): List<String> {
        val dbFieldList = mutableListOf<String>()
        fieldNameList.forEach {
            // 获取实体类属性
            val field = clazz.getDeclaredField(it)
            // 获取数据库字段
            var dbField = fieldMap[field]
            if (dbField == null) {
                dbField = MybatisUtil.getDbField(field)
            }
            dbFieldList.add(dbField)
        }
        return dbFieldList
    }

    /**
     * 解析排序
     */
    @JvmStatic
    fun analysisOrder(sortName: String?, sortOrder: String?, clazz: Class<*>? = null): List<Sort>? {
        if (StringUtils.isEmpty(sortName)) {
            return null
        }
        val sortList = mutableListOf<Sort>()
        val sortNames = sortName!!.split(",")
        var sortOrders: List<String> = listOf()
        if (sortOrder != null) {
            sortOrders = sortOrder.split(",")
        }
        for (index in 0 until sortNames.size) {
            sortList.add(Sort(sortNames[index].trim(), if (sortOrders.size > index) sortOrders[index].trim() else null))
        }
        if (clazz != null) {
            sortList.forEach {
                val dbName = MybatisUtil.getDbField(clazz, it.name)
                it.dbName = dbName
            }
        }
        return  sortList
    }
}