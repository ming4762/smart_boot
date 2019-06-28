package com.smart.common.utils

import java.util.*

/**
 * UUID 工具类
 * @author ming
 * 2019/6/12 上午10:00
 */
object UUIDGenerator {

    /**
     * 生成UUID
     */
    @JvmStatic fun getUUID(): String {
        var s = UUID.randomUUID().toString()
        s = s.replace("-", "")
        return s
    }

    /**
     * 生成UUID 指定位数
     * @param number 位数
     */
    @JvmStatic fun getUUID(number: Int): Array<String?>? {
        if (number < 1) {
            return null
        }
        val ss = arrayOfNulls<String>(number)
        for (i in 0 until number) {
            ss[i] = getUUID()
        }
        return ss
    }
}