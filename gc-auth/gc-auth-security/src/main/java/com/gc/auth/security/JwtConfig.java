package com.gc.auth.security;

import com.gc.auth.security.controller.LoginController;
import com.gc.auth.security.filter.JwtAuthenticationFilter;
import com.gc.auth.security.service.AuthService;
import com.gc.cache.service.CacheService;
import com.gc.common.auth.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * jwt配置
 * @author jackson
 * 2020/2/15 1:36 下午
 */
@Configuration
@ConditionalOnProperty(prefix = "gc.auth", name = "jwt", havingValue = "true")
public class JwtConfig {

    /**
     * 创建jwt拦截器
     * @param authProperties 认证参数
     * @param authService 认证服务
     * @return jwt拦截器
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(@Autowired AuthProperties authProperties, @Autowired AuthService authService) {
        return new JwtAuthenticationFilter(authService, authProperties);
    }

    /**
     * 创建认证服务
     * @param authProperties 认证参数
     * @param cacheService 缓存服务
     * @return 认证服务
     */
    @Bean
    public AuthService authService(@Autowired AuthProperties authProperties, @Autowired CacheService cacheService) {
        return new AuthService(authProperties, cacheService);
    }

    @Bean
    public LoginController loginController(@Autowired AuthService authService, @Autowired AuthenticationManager authenticationManager) {
        return new LoginController(authenticationManager, authService);
    }
}
