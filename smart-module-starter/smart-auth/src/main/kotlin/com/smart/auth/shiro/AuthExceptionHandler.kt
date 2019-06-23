package com.smart.auth.shiro

import com.alibaba.fastjson.JSON
import com.smart.common.message.Result
import org.apache.shiro.authz.AuthorizationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.security.auth.message.AuthException
import javax.servlet.http.HttpServletResponse

/**
 * 认证失败异常处理类
 * @author ming
 * 2019/6/22 下午2:09
 */
@ControllerAdvice
class AuthExceptionHandler {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AuthException :: class.java)
    }

    /**
     * shiro权限不足异常处理
     */
    @ExceptionHandler(AuthorizationException :: class)
    fun authErrorException(response: HttpServletResponse, e: Exception) {
        LOGGER.warn(e.message)
        response.status = HttpStatus.OK.value()
        response.contentType = "application/json; charset=utf-8"
        val writer = response.writer
        writer.write(JSON.toJSONString(Result.failure(403, e.message, null)))
        writer.close()
    }
}