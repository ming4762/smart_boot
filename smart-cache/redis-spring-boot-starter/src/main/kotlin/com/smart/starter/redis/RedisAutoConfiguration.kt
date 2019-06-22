package com.smart.starter.redis

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.smart.starter.redis.service.RedisService
import com.smart.starter.redis.service.impl.RedisServiceImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer

/**
 *
 * @author ming
 * 2019/6/22 下午3:34
 */

@Configuration
@ConditionalOnClass(RedisService :: class)
class RedisAutoConfiguration {

    /**
     * 创建redisTemplate
     */
    @Bean
    fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(factory)
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(Any :: class.java)
        val om = ObjectMapper()
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
        jackson2JsonRedisSerializer.setObjectMapper(om)
        redisTemplate.keySerializer = jackson2JsonRedisSerializer
        return redisTemplate
    }

    /**
     * 注入redis服务
     */
    @Bean
    @ConditionalOnMissingBean(RedisService :: class)
    fun redisService(): RedisService {
        return RedisServiceImpl()
    }
}