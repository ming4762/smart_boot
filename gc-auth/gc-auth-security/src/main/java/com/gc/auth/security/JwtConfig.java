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
 * @author jackson
 * 2020/2/15 1:36 下午
 */
@Configuration
@ConditionalOnProperty(prefix = "gc.auth", name = "jwt", havingValue = "true")
public class JwtConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(@Autowired AuthProperties authProperties, @Autowired AuthService authService) {
        return new JwtAuthenticationFilter(authService, authProperties);
    }

    @Bean
    public AuthService authService(@Autowired AuthProperties authProperties, @Autowired CacheService cacheService) {
        return new AuthService(authProperties, cacheService);
    }

    @Bean
    public LoginController loginController(@Autowired AuthService authService, @Autowired AuthenticationManager authenticationManager) {
        return new LoginController(authenticationManager, authService);
    }
}
