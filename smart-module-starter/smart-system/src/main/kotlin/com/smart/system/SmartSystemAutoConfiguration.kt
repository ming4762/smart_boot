package com.smart.system

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * 系统模块配置
 * @author ming
 * 2019/6/12 下午2:33
 */
@Configuration
@ComponentScan
@MapperScan("com.smart.system.mapper")
class SmartSystemAutoConfiguration {
}