package com.smart.starter.ide

import com.smart.starter.ide.filter.ParameterDecrypedFilter
import com.smart.starter.ide.interceptor.ParameterDecrypedInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.Filter

/**
 * 自动配置类
 * @author ming
 * 2019/7/24 下午4:46
 */
@Configuration
@EnableConfigurationProperties(IdeProperties::class)
@ComponentScan
@ConditionalOnProperty(prefix = "smart.auth.ide", name = ["enable"], havingValue = "true")
class IdeAutoConfiguration  : WebMvcConfigurer {

    @Autowired
    private lateinit var ideProperties: IdeProperties

    /**
     * 配置拦截器
     */
    @Bean
    fun filterRegistrationBean(): FilterRegistrationBean<Filter> {
        val registration = FilterRegistrationBean<Filter>()
        registration.filter = ParameterDecrypedFilter()
        registration.setName("parameterDecrypedFilter")
        return registration
    }

    /**
     * 添加拦截器
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 添加接口加密拦截器
        registry.addInterceptor(ParameterDecrypedInterceptor(this.ideProperties))
        super.addInterceptors(registry)
    }
}