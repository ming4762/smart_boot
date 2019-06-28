package com.smart.common.utils

/**
 * 反射工具类
 * @author ming
 * 2019/6/12 上午11:07
 */
object ReflectUtil {
    /**
     * 根据属性名获取属性值
     */
    @JvmStatic
    fun getFieldValue(value: Any, fieldName: String): Any? {
        val field = value :: class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(value)
    }
}