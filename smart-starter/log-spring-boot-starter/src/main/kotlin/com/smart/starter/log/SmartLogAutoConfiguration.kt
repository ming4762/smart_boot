package com.smart.starter.log

import com.smart.starter.log.aspect.LogAspect
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * 日志配置类
 * @author ming
 * 2019/6/28 下午4:01
 */
@Configuration
@EnableConfigurationProperties(LogProperties :: class)
@ComponentScan
@MapperScan("com.smart.starter.log.mapper", sqlSessionTemplateRef = "systemSqlSessionTemplate")
class SmartLogAutoConfiguration {

    /**
     * 创建日志切面
     */
    @Bean
    fun logAspect() = LogAspect()
}