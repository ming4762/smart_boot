package com.smart.common.utils.security

import org.apache.commons.codec.digest.DigestUtils
import java.io.IOException
import java.io.InputStream

/**
 * MD5 工具类
 * @author ming
 * 2019/6/12 上午10:21
 */
object MD5Utils {

    /**
     * MD5加密
     */
    fun md5(value: String, hashIterations: Int = 1): String {
        var hash = DigestUtils.md5Hex(value)
        if (hashIterations > 1) {
            for (i in 1 until hashIterations) {
                hash = DigestUtils.md5Hex(value)
            }
        }
        return hash
    }

    /**
     * 进行MD5加密
     * @param inputStream
     * @return
     */
    @Throws(IOException::class)
    fun md5(inputStream: InputStream): String {
        return DigestUtils.md5Hex(inputStream)
    }
}