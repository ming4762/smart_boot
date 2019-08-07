package com.smart.common.utils.http

import com.smart.common.spring.ApplicationContextRegister
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

/**
 * rest工具类
 * @author zhongming
 * @since 3.0
 * 2018/6/5下午2:02
 */
object RestUtil {

    /**
     * 发送post请求
     */
    fun restPost(url: String): ResponseEntity<*> {
        return restPost(url, null, null)
    }

    /**
     * 发送post请求
     * @param url
     * @param headers
     * @param parameters
     * @return
     */
    @JvmStatic
    fun restPost(url: String, headers: Map<String, String>?, parameters: MultiValueMap<String, Any>?): ResponseEntity<*> {

        val restTemplate = ApplicationContextRegister.getBean(RestTemplate::class.java) ?: throw Exception("获取restTemplate失败")
        //设置请求头
        val httpHeaders = HttpHeaders()
        if (headers != null) {
            for ((key, value) in headers) {
                httpHeaders.add(key, value)
            }
        }
        val requestEntity = HttpEntity(parameters, httpHeaders)
        return restTemplate.exchange<Any>(url, HttpMethod.POST, requestEntity, Any :: class.java)
    }

    /**
     * 发送get请求
     * @param url 请求地址
     * @param headers 请求头
     * @param parameters 请求参数
     * @return
     */
    @JvmStatic
    fun restGet(url: String, headers: Map<String, String>?, parameters: MultiValueMap<String, Any>): ResponseEntity<*> {
        val restTemplate = ApplicationContextRegister.getBean(RestTemplate::class.java) ?: throw Exception("获取restTemplate失败")
        //设置请求头
        val httpHeaders = HttpHeaders()
        if (headers != null) {
            for ((key, value) in headers) {
                httpHeaders.add(key, value)
            }
        }
        val requestEntity = HttpEntity(parameters, httpHeaders)
        return restTemplate.exchange<Any>(url, HttpMethod.GET, requestEntity, Any::class.java)
    }


    /**
     * 发送post请求
     * @param url
     * @param headers
     * @param parameters
     * @return
     */
    @JvmStatic
    fun restPost(url: String, headers: Map<String, String>, parameters: String?): ResponseEntity<*> {

        val restTemplate = ApplicationContextRegister.getBean(RestTemplate::class.java) ?: throw Exception("获取restTemplate失败")
        //设置请求头
        val httpHeaders = HttpHeaders()
        for ((key, value) in headers) {
            httpHeaders.add(key, value)
        }
        val requestEntity = HttpEntity(parameters, httpHeaders)
        return restTemplate.exchange<Any>(url, HttpMethod.POST, requestEntity, Any::class.java)
    }
}
