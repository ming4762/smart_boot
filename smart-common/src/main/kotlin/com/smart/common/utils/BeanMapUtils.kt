package com.smart.common.utils

import com.alibaba.fastjson.JSON
import java.lang.reflect.Modifier

/**
 * beanmap工具类
 */
object BeanMapUtils {

    /**
     * 转换对象
     * 常用于数据传输对象转为VO对象
     */
    @JvmStatic
    fun <T> convertBean(bean: Any, config: Map<String, String>, clazz: Class<T>): T? {
        // 将bean转为map
        val map = bean2Map(bean)
        // 将map根据配置转为另一个map
        val resultMap = convertMap(map, config)
        // 将map转为bean
        val json = JSON.toJSON(resultMap)
        return this.map2Bean(resultMap, clazz)
    }

    /**
     * 转换map
     */
    @JvmStatic
    fun convertMap(map: Map<String, Any?>, config: Map<String, String>): Map<String, Any?> {
        val result = mutableMapOf<String, Any?>()
        config.forEach { key, value ->
            // 获取原map的值
            val targetValue = getValue(map, value)
            // 设置值
            this.setValue(result, key.split("."), targetValue)
        }
        return result
    }

    /**
     * 设置值
     */
    @JvmStatic
    private fun setValue(targetMap: MutableMap<String, Any?>, keys: List<String>, value: Any?) {
        if (keys.isNotEmpty()) {
            if (keys.size == 1) {
                targetMap[keys[0]] = value
            } else {
                val secondMap = mutableMapOf<String, Any?>()
                targetMap[keys[0]] = secondMap
                setValue(secondMap, keys.subList(1, keys.size), value)
            }
        }
    }

    /**
     * 获取value
     */
    @JvmStatic
    private fun getValue(map: Map<String, Any?>, key: String): Any? {
        val keys = key.split(".")
        return getValue(map, keys)
    }

    /**
     * 遍历获取value
     */
    @JvmStatic
    private fun getValue(map: Map<String, Any?>, keys: List<String>): Any? {
        if (keys.size == 1) {
            return map[keys[0]]
        } else {
            val secondMap = map[keys[0]] as Map<String, Any>?
            if (secondMap == null) {
                return null
            } else {
                return getValue(secondMap, keys.subList(1, keys.size))
            }
        }
    }

    /**
     * bean转map
     */
    @JvmStatic
    fun bean2Map(bean: Any): Map<String, Any> {
        val json = JSON.toJSONString(bean)
        return JSON.parse(json) as Map<String, Any>
    }

    /**
     * map转bean
     */
    @JvmStatic
    fun <T> map2Bean(map: Map<String, Any?>, clazz: Class<T>): T? {
        return JSON.parse(JSON.toJSONString(map)) as T
    }

    /**
     * 从父类创建子类
     */
    @JvmStatic
    fun <T> createFromParent (parent: Any, clazz: Class<T>): T {
        val parentFieldMap = clazz.superclass.declaredFields
                .map { it.name to it }.toMap()
        // 获取子类属性
        val childrenFieldList = clazz.declaredFields.toMutableList()
        childrenFieldList.addAll(parentFieldMap.values)
        val child = clazz.newInstance()
        childrenFieldList.forEach {
            if (!Modifier.isFinal(it.modifiers)) {
                it.isAccessible = true
                it.set(child, parentFieldMap[it.name]?.get(parent))
            }
        }
        return child
    }

    /**
     * 从相同属性创建对象
     */
    fun <T> createSameField(form: Any, clazz: Class<T>): T {
        // 获取原对象属性并转为map
        val formFieldMap = form :: class.java.declaredFields
                .map { it.name to it }
                .toMap()
        // 创建对象
        val result = clazz.newInstance()
        // 获取结果对象的field
        val resultFieldList = clazz.declaredFields.toMutableList()
        resultFieldList.forEach {
            val name = it.name
            val formField = formFieldMap[name]
            if (formField != null && !Modifier.isFinal(it.modifiers) && formField.type == it.type) {
                it.isAccessible = true
                formField.isAccessible = true
                it.set(result, formField.get(form))
            }
        }
        return result
    }

}