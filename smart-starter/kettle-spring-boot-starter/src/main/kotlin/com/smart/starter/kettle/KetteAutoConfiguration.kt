package com.smart.starter.kettle

import com.smart.starter.kettle.pool.KettleRepositoryProvider
import com.smart.starter.kettle.pool.impl.KettleRepositoryProviderImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * kettle自动配置类
 * @author ming
 * 2019/7/11 上午9:43
 */
@Configuration
@EnableConfigurationProperties(KettleProperties::class)
@ComponentScan
class KetteAutoConfiguration {

    @Autowired
    private lateinit var kettleProperties: KettleProperties

    /**
     * 创建资源库提供者
     */
    @Bean
    @ConditionalOnMissingBean(KettleRepositoryProvider::class)
    fun kettleRepositoryProvider(): KettleRepositoryProvider {
        return KettleRepositoryProviderImpl(this.kettleProperties.repository)
    }
}