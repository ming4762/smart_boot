package com.smart.starter.ide.advice

import com.alibaba.fastjson.JSON
import com.smart.starter.ide.annotation.ParameterSecurity
import com.smart.starter.ide.util.IDEUtils
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice
import java.io.InputStream
import java.lang.reflect.Type
import java.nio.charset.Charset


/**
 * 参数解密
 * @author zhongming
 */
@ControllerAdvice
class RsaDecodeRequestBodyAdvice: RequestBodyAdvice {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RsaDecodeRequestBodyAdvice :: class.java)
    }

    override fun afterBodyRead(body: Any, inputMessage: HttpInputMessage, methodParameter: MethodParameter, type: Type, clazz: Class<out HttpMessageConverter<*>>): Any {
        return body
    }

    override fun beforeBodyRead(inputMessage: HttpInputMessage, methodParameter: MethodParameter, type: Type, clazz: Class<out HttpMessageConverter<*>>): HttpInputMessage {

        // 是否需要解密
        var encode = false
        if (methodParameter.method?.isAnnotationPresent(ParameterSecurity :: class.java) == true) {
            // 获取注解
            val rsaSecurity = methodParameter.getMethodAnnotation(ParameterSecurity :: class.java)!!
            encode = rsaSecurity.inDecode
        }
        if (encode) {
            return SecurityHttpInputMessage(inputMessage, JSON.toJSONString(IDEUtils.get()!!.data!!))
        }
        return inputMessage
    }

    override fun handleEmptyBody(p0: Any?, inputMessage: HttpInputMessage, methodParameter: MethodParameter, p3: Type, p4: Class<out HttpMessageConverter<*>>): Any? {

        return p0
    }

    override fun supports(p0: MethodParameter, p1: Type, p2: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    /**
     * 获取用户秘钥信息
     */

    /**
     * 加密的HttpInputMessage
     */
    class SecurityHttpInputMessage // 通过私钥进行解密
    (inputMessage: HttpInputMessage, data: String) : HttpInputMessage {

        private var headers: HttpHeaders = inputMessage.headers

        private var body: InputStream = IOUtils.toInputStream(data, Charset.forName("UTF-8"))

        override fun getHeaders(): HttpHeaders {
            return this.headers
        }

        override fun getBody(): InputStream {
            return this.body
        }
    }
}