package com.gc.auth.extensions.jwt.config;

import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.core.service.AuthCache;
import com.gc.auth.extensions.jwt.service.JwtService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/1/8 15:59
 * @since 1.0
 */
@Configuration
public class AuthJwtBeanConfig {

    /**
     * 创建JwtService
     * @param authProperties 参数
     * @param authCache 缓存工具
     * @return JwtService
     */
    @Bean
    @ConditionalOnMissingBean(JwtService.class)
    public JwtService jwtService(AuthProperties authProperties, AuthCache<String, Object> authCache) {
        return new JwtService(authProperties, authCache);
    }
}
