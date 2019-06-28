package com.smart.common.utils

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.fluent.Request
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIUtils
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.slf4j.LoggerFactory
import org.springframework.util.StringUtils
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.ArrayList

/**
 * http工具类，基于org.apache.http
 * @author ming
 * 2019/6/12 上午10:06
 */
object HttpUtil {

    private val logger = LoggerFactory.getLogger(HttpUtil::class.java)

    /**
     * 发送post请求
     * @param url 请求的URL
     * @param headers 请求头信息
     * @param parameters 请求参数
     * @return 请求结果
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun httpPost(url: String, headers: Map<String, String?>, parameters: Map<String, Any?>): HttpEntity? {
        val httpClient = HttpClients.createDefault()
        val post = HttpPost(url)
        //设置请求头
        for ((key, value) in headers) {
            post.setHeader(key, value)
        }

        //设置参数
        val nvps = ArrayList<NameValuePair>()
        //设置参数
        for ((key, value) in parameters) {
            nvps.add(BasicNameValuePair(key, value.toString()))
        }
        post.entity = UrlEncodedFormEntity(nvps, "utf-8")
        val response = httpClient.execute(post)
        //获取状态码
        if (response.statusLine.statusCode == HttpStatus.SC_OK) {
            return response.entity
        }
        logger.warn("发送POST请求失败，URL：{}，请求头：{}，请求参数：{}，错误码：{}", url, headers, parameters, response.statusLine.statusCode)
        return null

    }


    /**
     * 发送post请求
     * @param url 请求的URL
     * @param headers 请求头信息
     * @param parameters 请求参数
     * @return 请求结果
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun httpGet(url: String, headers: Map<String, String>? = null, parameters: Map<String, Any>? = null): HttpResponse {
        val httpClient = HttpClients.createDefault()
        val stringBuffer = StringBuilder(url)
        if (parameters != null) {
            //设置参数
            var i = 0
            for ((key, value) in parameters) {
                if (i == 0) {
                    stringBuffer.append("?").append(key).append("=").append(value)
                } else {
                    stringBuffer.append("&").append(key).append("=").append(value)
                }
                i++
            }
        }

        val get = HttpGet(stringBuffer.toString())
        if (headers != null) {
            //设置请求头
            for ((key, value) in headers) {
                get.setHeader(key, value)
            }
        }

        val response = httpClient.execute(get)
        //获取状态码
        if (response.statusLine.statusCode != HttpStatus.SC_OK) {
            logger.warn("发送GET请求失败，URL：{}，请求头：{}，请求参数：{}，错误码：{}", url, headers, parameters, response.statusLine.statusCode)
        }
        return response

    }

    @Throws(IOException::class)
    @JvmStatic
    fun downFile(src: String): InputStream {
        return downFile(URI.create(src))
    }

    /**
     * 从网络上下载文件
     *
     * @param uri
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic fun downFile(uri: URI): InputStream {
        val httpResponse: HttpResponse
        try {
            val request = Request.Get(uri)
            val httpHost = URIUtils.extractHost(uri)
            if (!StringUtils.isEmpty(httpHost.hostName)) {
                request.setHeader("Host", httpHost.hostName)
            }
            request.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

            httpResponse = request.execute().returnResponse()
        } catch (e: Exception) {
            logger.error("远程请求失败，url=$uri", e)
            throw FileNotFoundException()
        }

        val code = httpResponse.statusLine.statusCode
        if (code != 200) {
            throw FileNotFoundException()
        }

        return httpResponse.entity.content
    }
}