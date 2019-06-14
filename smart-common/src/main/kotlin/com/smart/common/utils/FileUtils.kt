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
}