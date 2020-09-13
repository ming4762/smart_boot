package com.gc.auth.cache.guava;

import com.gc.auth.cache.guava.cache.GuavaAuthCache;
import com.gc.common.auth.properties.AuthProperties;
import com.gc.common.auth.service.AuthCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/9/11 9:38 下午
 */
@Configuration
@ComponentScan
public class AuthCacheGuavaAutoConfiguration {

    /**
     * 创建guavaAuthCache
     * @param authProperties authProperties
     * @return guavaAuthCache
     */
    @Bean
    public AuthCache<String, Object> guavaAuthCache(AuthProperties authProperties) {
        return new GuavaAuthCache(authProperties);
    }
}
