package com.smart.common.utils

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

/**
 * Base64Util 工具类
 * @author ming
 * 2019/6/12 上午9:42
 */
object Base64Util {
    //解码工具
    private val DECODER = Base64.getDecoder()

    //转码工具
    private val ENCODER = Base64.getEncoder()

    /**
     * base64编码
     * @param str 要编码的字符串
     * @return 编码后的字符串
     */
    @Throws(UnsupportedEncodingException::class)
    @JvmStatic
    fun encoder(str: String): String {
        val textByte = str.toByteArray(charset("UTF-8"))
        return encoder(textByte)
    }

    /**
     * base64编码
     * @param bytes 要编码的字节数组
     * @return 编码后的字符串
     */
    @JvmStatic
    fun encoder(bytes: ByteArray): String {
        return ENCODER.encodeToString(bytes)
    }

    /**
     * base64解码
     * @param str 要解码的字符串
     * @return 解码后的字符串
     * @throws UnsupportedEncodingException
     */
    @Throws(UnsupportedEncodingException::class)
    @JvmStatic
    fun decoder(str: String): String {
        return String(DECODER.decode(str), Charset.forName("UTF-8"))
    }
}