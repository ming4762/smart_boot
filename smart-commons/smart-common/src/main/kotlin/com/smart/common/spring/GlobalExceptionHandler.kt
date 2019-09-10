package com.smart.common.spring

import com.smart.common.message.Result
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * 全局异常处理
 * @author ming
 * 2019/9/10 上午9:42
 */
@ControllerAdvice
@ConditionalOnMissingBean(GlobalExceptionHandler :: class)
class GlobalExceptionHandler {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler :: class.java)
    }

    /**
     * 异常处理
     */
    @ResponseBody
    @ExceptionHandler(Exception :: class)
    fun handleException(e: Exception): Result<Any?> {
        LOGGER.error(e.toString())
        return Result.failure(e.message)
    }
}