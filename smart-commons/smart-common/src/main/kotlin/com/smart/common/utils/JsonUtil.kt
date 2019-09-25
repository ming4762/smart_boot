package com.smart.common.utils

import com.alibaba.fastjson.JSON

/**
 *
 * @author ming
 * 2019/9/15 下午8:14
 */
object JsonUtil {

    /**
     * 转换java对象
     */
    fun <T> toJavaObject(data: Any, clazz: Class<T>): T {
        return JSON.parseObject(JSON.toJSONString(data), clazz)
    }
}