package com.smart.ui.common

import com.smart.ui.common.interceptor.EnvInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 *
 * @author ming
 * 2019/8/14 上午10:22
 */
@Configuration
@ComponentScan
class CommonAutoConfiguration : WebMvcConfigurer {

    @Value("\${spring.profiles.active:}")
    private lateinit var profiles: String

    @Value("\${smart.env.development:}")
    private lateinit var development: String

    /**
     * 添加环境设置拦截器
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(EnvInterceptor(this.profiles, this.development))
        super.addInterceptors(registry)
    }
}