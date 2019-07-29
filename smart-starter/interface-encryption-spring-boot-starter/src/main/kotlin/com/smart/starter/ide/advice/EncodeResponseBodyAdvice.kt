package com.smart.starter.ide.advice


import com.fasterxml.jackson.databind.ObjectMapper
import com.smart.common.utils.security.RSAUtils
import com.smart.starter.ide.IdeProperties
import com.smart.starter.ide.annotation.ParameterSecurity
import com.smart.starter.ide.util.IDEUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice


/**
 * 返回参数加密
 * @author zhongming
 */
@ControllerAdvice
class EncodeResponseBodyAdvice: ResponseBodyAdvice<Any> {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EncodeResponseBodyAdvice :: class.java)
    }

    @Autowired
    private lateinit var ideProperties: IdeProperties

    override fun supports(methodParameter: MethodParameter, clazz: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(body: Any?, methodParameter: MethodParameter, dediaType: MediaType, clazz: Class<out HttpMessageConverter<*>>, request: ServerHttpRequest, response: ServerHttpResponse): Any? {
        // 标识是否加密
        var encode = false
        if (methodParameter.method?.isAnnotationPresent(ParameterSecurity :: class.java) == true) {
            // 获取注解
            val rsaSecurity = methodParameter.getMethodAnnotation(ParameterSecurity :: class.java)!!
            encode = rsaSecurity.outEncode
        }
        if (encode) {
            LOGGER.info("对方法：【${methodParameter.method?.name}】返回数据进行加密")
            val objectMapper = ObjectMapper()
            try {
                val result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body)
                // 获取客户端公钥
                val clientPubKey = IDEUtils.getClientPublicKey()
                return if (clientPubKey == null) {
                    LOGGER.warn("方法method：【${methodParameter.method?.name}】结果加密失败，未找到公钥")
                    result
                } else {
                    RSAUtils.encryptByPublicKey(result, clientPubKey)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LOGGER.error("方法method：【${methodParameter.method?.name}】结果加密失败，错误信息：${e.message}")
            }
        }
        return body
    }
}