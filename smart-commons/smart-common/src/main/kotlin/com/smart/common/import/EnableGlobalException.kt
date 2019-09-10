package com.smart.common.import

import com.smart.common.spring.GlobalExceptionHandler
import org.springframework.context.annotation.Import

/**
 * 全局异常处理
 * @author ming
 * 2019/9/10 上午9:47
 */
@Import(GlobalExceptionHandler ::class)
annotation class EnableGlobalException {
}