package com.smart.starter.ide.interceptor

import com.alibaba.fastjson.JSON
import com.smart.common.model.RsaKey
import com.smart.common.utils.security.MD5Utils
import com.smart.common.utils.security.RSAUtils
import com.smart.starter.ide.IdeProperties
import com.smart.starter.ide.annotation.ParameterSecurity
import com.smart.starter.ide.model.EncrypParameter
import com.smart.starter.ide.util.IDEUtils
import com.smart.starter.ide.util.RsaKeyUtils
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.io.InputStream
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 参数解密验证拦截器拦截器
 * @author ming
 * 2019/7/24 下午5:08
 */
class ParameterDecrypedInterceptor(val ideProperties: IdeProperties) : HandlerInterceptorAdapter() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ParameterDecrypedInterceptor :: class.java)
        // 公共秘钥
        private var rsaKey: RsaKey? = null
    }


    /**
     * 1、判断目标方法是否需要解密
     * 2、验证解密信息，MD5、时间戳
     * 3、将解密信息放入线程缓存中，由RequestBodyAdvice放入参数内
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            // 获取注解 判断是否需要参数解密
            val parameterSecurity = handler.getMethodAnnotation(ParameterSecurity :: class.java)
            if (parameterSecurity != null) {
                if (parameterSecurity.inDecode) {
                    // 获取参数解密结果
                    val encrypParameter = try {
                        this.decryptParameter(request.inputStream, handler)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                    if (encrypParameter == null) {
                        val message = "对方法method：【${handler.method.name}】参数进行解密发生错误"
                        LOGGER.info(message)
                        response.sendError(500, message)
                        return false
                    } else {
                        // 进行MD5校验
                        val md5Check = this.md5Check(parameterSecurity, encrypParameter)
                        if (!md5Check) {
                            val md5Message = "方法method：【${handler.method.name}】md5校验失败"
                            LOGGER.debug(md5Message)
                            response.sendError(401, md5Message)
                            return false
                        }

                        // 进行时间戳校验
                        val timestampCheck = this.timestampCheck(parameterSecurity, encrypParameter)
                        if (!timestampCheck) {
                            val timestampCheckMessage = "方法method：【${handler.method.name}】时间戳校验失败"
                            LOGGER.debug(timestampCheckMessage)
                            response.sendError(401, timestampCheckMessage)
                            return false
                        }

                        // 将结果放入线程缓存中
                        IDEUtils.set(encrypParameter)
                    }
                }
            }
        }
        return super.preHandle(request, response, handler)
    }

    /**
     * 参数解密
     */
    private fun decryptParameter(inputStream: InputStream, handler: HandlerMethod): EncrypParameter? {
        LOGGER.info("对方法method：【${handler.method.name}】返回数据进行解密")
        // 获取私钥
        val priKey = RsaKeyUtils.getServerKey().priKey
        val content = IOUtils.toString(inputStream, "utf-8")
        // 解密
        val decryptValue = RSAUtils.decryptByPrivateKey(content, priKey)
        return JSON.parseObject(decryptValue, EncrypParameter :: class.java)
    }


    /**
     * MD5验证
     */
    private fun md5Check(parameterSecurity: ParameterSecurity, encrypParameter: EncrypParameter): Boolean {
        var checkResult = true
        if (parameterSecurity.md5) {
            // 获取前端MD5值
            val userMd5 = encrypParameter.md5
            // 获取数据MD5值
            val dataMd5 = MD5Utils.md5(JSON.toJSONString(encrypParameter.data))
            if (userMd5 != dataMd5) {
                checkResult = false
            }
        }
        return checkResult
    }

    /**
     * 时间戳校验
     */
    private fun timestampCheck(parameterSecurity: ParameterSecurity, encrypParameter: EncrypParameter): Boolean {
        var checkResult = true
        if (parameterSecurity.timestamp) {
            if (encrypParameter.timestamp == null) return false
            checkResult = Date().time - encrypParameter.timestamp!! <= this.ideProperties.timeDiff
        }
        return checkResult
    }
}




