
package com.smart.starter.crud.utils

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.starter.crud.model.BaseModel
import com.smart.starter.crud.model.Sort
import com.smart.starter.crud.query.QueryParameter
import org.slf4j.LoggerFactory
import org.springframework.util.StringUtils
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties
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
    @JvmStatic
    private val wrapperMethodParameterMap = mapOf(
            "=" to SymbolParameterType("eq", arrayOf(Any::class.java, Any::class.java)),
            "like" to SymbolParameterType("like", arrayOf(Any::class.java, Any::class.java)),
            ">" to SymbolParameterType("gt", arrayOf(Any::class.java, Any::class.java)),
            ">=" to SymbolParameterType("ge", arrayOf(Any::class.java, Any::class.java)),
            "<>" to SymbolParameterType("ne", arrayOf(Any::class.java, Any::class.java)),
            "<" to SymbolParameterType("lt", arrayOf(Any::class.java, Any::class.java)),
            "<=" to SymbolParameterType("le", arrayOf(Any::class.java, Any::class.java)),
            "in" to SymbolParameterType("in", arrayOf(Any::class.java, Collection::class.java)),
            "notLike" to SymbolParameterType("notLike", arrayOf(Any::class.java, Any::class.java)),
            "likeLeft" to SymbolParameterType("likeLeft", arrayOf(Any::class.java, Any::class.java)),
            "likeRight" to SymbolParameterType("likeRight", arrayOf(Any::class.java, Any::class.java)),
            "notIn" to SymbolParameterType("notIn", arrayOf(Any::class.java, Collection::class.java)),
            "groupBy" to SymbolParameterType("groupBy", arrayOf(Any::class.java))
    )

    // 实体类 类型/Class缓存
    private val typeClassCache = ConcurrentHashMap<Type, Class<out BaseModel>>()
    // 类型主键缓存
    private val typeModelKeysCache = ConcurrentHashMap<Type, List<String>>()
    // 存储字段信息
    @JvmStatic
    private val fieldDBFieldCache = ConcurrentHashMap<Field, String>()

    //存储类型跟keylist对应关系
    @JvmStatic
    private val typeKeyMap = mutableMapOf<Type, List<String>>()
    // 存储类型
    @JvmStatic
    @Deprecated("typeClassCache")
    private val classMap: MutableMap<String, Class<*>> = mutableMapOf()
    // 存储字段信息
    @JvmStatic
    private val fieldMap: MutableMap<Field, String> = mutableMapOf()
    // 存储kotlin字段信息
    @JvmStatic
    private val ktPropertyMap: MutableMap<String, KProperty<*>?> = mutableMapOf()



    /**
     * 获取实体类的ID信息
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    @Deprecated("getModelKeysByType")
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
     * 通过Type获取实体类的key
     */
    fun getModelKeysByType(type: Type): List<String> {
        // 从缓存中获取
        var keys = typeModelKeysCache[type]
        if (keys == null) {
            val clazz = getModelClassByType(type)
            keys = if (clazz != null) {
                getModelKeysByClass(clazz)
            } else {
                listOf()
            }
            typeModelKeysCache[type] = keys
        }
        return keys
    }

    /**
     * 通过Class获取实体类的key
     */
    @JvmStatic
    fun getModelKeysByClass(clazz: Class<out BaseModel>): List<String> {
        val keyList = mutableListOf<String>()
        clazz.declaredFields.forEach { field ->
            // 获取主键注解
            val tableId = field.getAnnotation(TableId :: class.java)
            if (tableId != null) {
                keyList.add(field.name)
            }
        }
        return keyList
    }

    /**
     * 获取实体类主键字段
     */
    @JvmStatic
    @Deprecated("getModelKeysByClass")
    fun getModelKeyField(clazz: Class<*>): List<String> {
        val keyList = mutableListOf<String>()
        // 获取类所有属性
        val fieldList = clazz.declaredFields
        var idField = ""
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
    @Suppress("DEPRECATION")
    @JvmStatic
    @Deprecated("getModelClassByType")
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
     * 通过实体类类型获取实体类
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun getModelClassByType(type: Type): Class<out BaseModel>? {
        var clazz = typeClassCache[type]
        if (clazz == null) {
            val clazzGet = Class.forName(type.typeName)
            val isChild = BaseModel :: class.java.isAssignableFrom(clazzGet)
            if (isChild) {
                clazz = clazzGet as Class<BaseModel>
                typeClassCache[type] = clazz
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
        var dbField = fieldDBFieldCache[field]
        if (dbField == null) {
            // 获取注解值
            dbField = field.getAnnotation(TableField :: class.java)?.value
            if (StringUtils.isEmpty(dbField)) {
                // 如果未指定数据库字段，通过驼峰表示获取
                dbField = getDefaultDbField(field.name)
            }
            fieldDBFieldCache[field] = dbField!!
        }

        return dbField
    }


    /**
     * 获取数据库字段
     */
    @JvmStatic
    fun getDbField(clazz: Class<out BaseModel>, fieldName: String): String? {
        val field = clazz.getDeclaredField(fieldName) ?: return null
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
    fun <T: BaseModel> createQueryWrapperFromParameters(parameters: QueryParameter, type: Type): QueryWrapper<T> {
        val queryWrapper = QueryWrapper<T>()
        val clazz = this.getModelClassByType(type)
        if (clazz != null) {
            return MybatisUtil.createQueryWrapperFromParameters(parameters, clazz as Class<T>)
        }
        return queryWrapper
    }

    /**
     * 从参数中创建查询条件
     */
    @JvmStatic
    fun <T: BaseModel> createQueryWrapperFromParameters(parameters: QueryParameter, clazz: Class<T>): QueryWrapper<T> {
        val queryWrapper = QueryWrapper<T>()
        MybatisUtil.createBaseQueryWrapperFromParameters(parameters, clazz, queryWrapper)
        return queryWrapper
    }


    /**
     * 从参数创建查询条件，kotlin
     */
    @JvmStatic
    fun <T: BaseModel> createKtQueryWrapperFromParameters(parameters: QueryParameter, clazz: KClass<T>): KtQueryWrapper<T> {
        val queryWrapper = KtQueryWrapper(clazz.java)
        MybatisUtil.createBaseQueryWrapperFromParameters(parameters, clazz, queryWrapper)
        return queryWrapper
    }

    /**
     * 从参数创建查询条件
     */
    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    @JvmStatic
    private fun <T: BaseModel> createBaseQueryWrapperFromParameters(parameters: QueryParameter, clazz: Any, wrapper: Wrapper<T>) {
        // 判断是否是kotlin
        val isKotlin = when (wrapper) {
            is QueryWrapper -> false
            is KtQueryWrapper -> true
            else -> throw Exception("参数错误，只支持QueryWrapper， KtQueryWrapper")
        }
        parameters.forEach { key, value ->
            if (key.contains("@")) {
                val keySplit = key.split("@")
                // 获取符号
                val symbol = if (keySplit.size > 1) keySplit[1] else null
                // 符号不存在则不处理
                if (symbol != null) {
                    // 获取第一个参数，实体类属性/数据库字段名
                    val parameter1 = if (isKotlin) {
                        MybatisUtil.getKPropertyByName(clazz as KClass<out BaseModel>, keySplit[0])
                    } else {
                        MybatisUtil.getDbField(clazz as Class<out BaseModel>, keySplit[0])
                    }
                    if (parameter1 == null) {
                        logger.warn("参数无效，未找到实体类对应属性：$keySplit[0]")
                    } else {
                        if (value != null) {
                            if (value != "") {
                                val method = this.getWrapperMethod(wrapper :: class.java, symbol)
                                if (method == null) {
                                    logger.warn("参数无效，未找到符号对应执行方法：$symbol")
                                } else {
                                    // 执行方法
                                    method.invoke(wrapper, parameter1, value)
                                }
                            } else {
                                logger.warn("参数无效，忽略参数值：$value")
                            }
                        } else {
                            // null 处理
                            if (symbol == "=") {
                                if (isKotlin) (wrapper as KtQueryWrapper).isNull(parameter1 as KProperty<*>)
                                else (wrapper as QueryWrapper).isNull(parameter1 as String)
                            } else if (symbol == "<>") {
                                if (isKotlin) (wrapper as KtQueryWrapper).isNotNull(parameter1 as KProperty<*>)
                                else (wrapper as QueryWrapper).isNotNull(parameter1 as String)
                            } else {
                                logger.warn("null值参数只能使用'='或'<>'")
                            }
                        }
                    }
                } else {
                    logger.warn("参数无效，未找到符号：$key")
                }
            }
        }
    }


    /**
     * 通过名字获取kotlin类属性
     * @param kClazz kotlin类对象
     * @param name 属性名
     */
    @JvmStatic
    fun getKPropertyByName(kClazz: KClass<out BaseModel>, name: String): KProperty<*>? {
        val qualifiedName = kClazz.qualifiedName
        if (qualifiedName != null) {
            // 获取缓存的key
            val key = "${qualifiedName}_$name"
            // 从缓存中获取属性，如果key存在即是属性为null也直接返回
            return if (ktPropertyMap.containsKey(key)) {
                ktPropertyMap[key]
            } else {
                val memberProperties = kClazz.memberProperties
                var kPropertyResult: KProperty<*>? = null
                for (kProperty in memberProperties) {
                    if (kProperty.name == name) {
                        kPropertyResult = kProperty
                        break
                    }
                }
                ktPropertyMap[key] = kPropertyResult
                kPropertyResult
            }
        }
        return null
    }

    /**
     * 获取数据库字段
     */
    @JvmStatic
    fun getDbFieldList(clazz: Class<out BaseModel>, fieldNameList: List<String>): List<String> {
        val dbFieldList = mutableListOf<String>()
        fieldNameList.forEach {
            // 获取实体类属性
            val field = clazz.getDeclaredField(it)
            // 获取数据库字段
            var dbField = fieldMap[field]
            // TODO: map缓存无效
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
    fun analysisOrder(sortName: String, sortOrder: String?, clazz: Class<out BaseModel>): List<Sort> {
        val sortNameList = sortName.split(",")
        val sortOrderList = sortOrder?.split(",") ?: listOf()

        val sortList = mutableListOf<Sort>()
        for (index in 0 until sortNameList.size) {
            sortList.add(Sort(sortNameList[index].trim(), if (sortOrderList.size > index) sortOrderList[index].trim() else "asc"))
        }
        sortList.filter {
            val dbName = MybatisUtil.getDbField(clazz, it.name)
            if (dbName != null) {
                it.dbName = dbName
            } else {
                logger.warn("未找到排序字段对应的数据库字段：${it.name}，该排序属性被忽略")
            }
            return@filter dbName != null
        }
        return sortList
    }

    /**
     * 获取方法
     * @param clazz 类
     */
    @JvmStatic
    private fun getWrapperMethod(clazz: Class<out Wrapper<out BaseModel>>, symbol: String): Method? {
        var method: Method? = null
        val symbolParameterType = wrapperMethodParameterMap[symbol]
        if (symbolParameterType != null) {
            method = clazz.getMethod(symbolParameterType.symbol, *symbolParameterType.parameterTypes)
        }
        return method
    }

    class SymbolParameterType(val symbol: String, val parameterTypes: Array<Class<out Any>>)
}