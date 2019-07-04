package com.smart.starter.queue

import com.smart.starter.queue.service.QueueExecuteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 队列自动配置类
 * @author ming
 * 2019/7/4 上午9:58
 */
@Configuration
@EnableConfigurationProperties(QueueProperties::class)
class SmartQueueAutoConfiguration {

    @Autowired
    private lateinit var queueProperties: QueueProperties

    /**
     * 创建队列服务
     */
    @Bean
    @ConditionalOnMissingBean(QueueExecuteService :: class)
    fun queueExecuteService() = QueueExecuteService(queueProperties)
}