package com.smart.common.utils

/**
 * 文件工具类
 * @author ming
 * 2019/6/12 上午9:44
 */
object FileUtils {

    /**
     * 获取文件扩展名
     */
    @JvmStatic
    fun getExtension(fileName: String): String {
        val stringList = fileName.split(".")
        return stringList[stringList.size - 1]
    }

    /**
     * 校验扩展名
     * @author zhongming
     */
    @JvmStatic
    fun validateExtension(fileName: String, allowExtensions: List<String>): Boolean {
        return allowExtensions.contains(getExtension(fileName))
    }
}