package com.smart.starter.fluorite.service

import com.smart.cache.common.service.CacheService
import com.smart.common.utils.http.RestUtil
import com.smart.starter.fluorite.FluoriteProperties
import com.smart.starter.fluorite.constants.AccessUrlConstants
import com.smart.starter.fluorite.constants.FluoriteConstants
import com.smart.starter.fluorite.exception.FluoriteApiException
import com.smart.starter.fluorite.model.AccessToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 *
 * @author ming
 * 2019/8/6 下午3:59
 */
@Service
class FluoriteService {

    @Autowired
    private lateinit var properties: FluoriteProperties

    /**
     * TODO: 待完善
     */
    @Autowired
    private lateinit var cacheService: CacheService


    /**
     * 获取token
     */
    fun getToken(): String? {
        // 从redis中获取token信息
        val accessToken = this.cacheService.get(FluoriteConstants.ACCESS_TOKEN_MESSAGE_KEY.name, AccessToken :: class.java)
        return accessToken?.accessToken ?: this.refreshToken()
    }

    /**
     * 刷新token
     */
    fun refreshToken(): String? {
        val accessToken = this.getTokenMessage()
        if (accessToken != null) {
            this.cacheService.put(FluoriteConstants.ACCESS_TOKEN_MESSAGE_KEY.name, accessToken, Date(accessToken.expireTime))
            return accessToken.accessToken
        } else {
            return null
        }
    }

    /**
     * 获取 token信息
     */
    private fun getTokenMessage(): AccessToken ?{
        val url = "${AccessUrlConstants.TOKEN_URL.url}?appKey=${properties.appKey}&appSecret=${properties.appSecret}"
        var result: Map<String, Any>? = null
        val response = RestUtil.restPost(url)
        if (response.statusCodeValue == 200) {
            val body =  response.body as Map<String, Any?>
            if (body["code"] == "200") {
                result =  body["data"] as Map<String, Any>?
            } else {
                throw FluoriteApiException(body.toString())
            }
        } else {
            throw RuntimeException(response.body.toString())
        }
        return result?.let {
            return@let AccessToken(result[FluoriteConstants.accessToken.name] as String, result[FluoriteConstants.expireTime.name] as Long)
        }
    }

    /**
     * 获取带有token的url
     */
    fun getUrlWithToken(url: String): String {
        return "$url?accessToken=${this.getToken()}"
    }

}