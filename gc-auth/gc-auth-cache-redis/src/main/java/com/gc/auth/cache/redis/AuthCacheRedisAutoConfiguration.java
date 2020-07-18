package com.gc.auth.cache.redis;

import com.gc.starter.redis.service.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/7/1 3:53 下午
 */
@Configuration
public class AuthCacheRedisAutoConfiguration {

    @Bean
    public RedisAuthCache redisAuthCache(RedisService redisService) {
        return new RedisAuthCache(redisService);
    }
}
