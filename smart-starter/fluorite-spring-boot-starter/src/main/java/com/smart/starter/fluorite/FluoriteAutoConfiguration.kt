package com.smart.starter.fluorite

import com.smart.common.import.EnableSpringContext
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

/**
 *
 * @author ming
 * 2019/8/6 下午3:28
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(FluoriteProperties :: class)
@EnableSpringContext
class FluoriteAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun restTemplate (factory: ClientHttpRequestFactory) = RestTemplate(factory)

    @Bean
    @ConditionalOnMissingBean
    fun simpleClientHttpRequestFactory (): ClientHttpRequestFactory {
        val factory = SimpleClientHttpRequestFactory()
        factory.setReadTimeout(5000)
        factory.setConnectTimeout(5000)
        return factory
    }
}