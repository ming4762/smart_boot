package com.smart.common.import

import com.smart.common.spring.ApplicationContextRegister
import org.springframework.context.annotation.Import

/**
 * 引入spring容器
 * @author ming
 * 2019/7/6 下午8:31
 */
@Import(ApplicationContextRegister :: class)
annotation class EnableSpringContext {
}