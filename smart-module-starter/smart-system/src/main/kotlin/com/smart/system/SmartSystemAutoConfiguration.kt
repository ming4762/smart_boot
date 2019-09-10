package com.smart.system

import com.smart.common.import.EnableGlobalException
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * 系统模块配置
 * @author ming
 * 2019/6/12 下午2:33
 */
@Configuration
@ComponentScan
@EnableGlobalException
class SmartSystemAutoConfiguration {
}