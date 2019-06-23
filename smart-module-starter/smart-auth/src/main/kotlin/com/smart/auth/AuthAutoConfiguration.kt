package com.smart.auth

import com.smart.auth.config.ShiroConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * 认证模块自动配置类
 * @author ming
 * 2019/6/21 下午1:44
 */
@Configuration
@EnableConfigurationProperties(AuthProperties :: class)
@Import(ShiroConfiguration :: class)
@ComponentScan
class AuthAutoConfiguration {
}