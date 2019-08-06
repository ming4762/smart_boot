package com.smart.starter.fluorite.utils

import com.smart.common.utils.http.RestUtil
import com.smart.starter.fluorite.exception.FluoriteApiException


object FluoriteRestUtil {

    /**
     * 拼接url
     */
    fun splicingUrl(baseUrl: String, paramater: Map<String, Any>): String {
        val buffer = StringBuilder(baseUrl)
        paramater.forEach { key, value ->
            buffer.append("&$key=$value")
        }
        return buffer.toString()
    }

    /**
     * 发送post请求
     */
    fun post (url: String): Map<String, Any> {
        val response = RestUtil.restPost(url)
        if (response.statusCodeValue != 200) {
            throw Exception("发送请求失败，url：$url")
        }
        val data = response.body as Map<String, Any>
        if (data["code"] != "200") {
            throw FluoriteApiException("发送请求失败，失败信息：${data.toString()}")
        }
        return data
    }
}