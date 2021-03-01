package com.gc.auth.extensions.jwt.config;

import com.gc.auth.core.authentication.RestAuthenticationProvider;
import com.gc.auth.core.handler.AuthLogoutSuccessHandler;
import com.gc.auth.core.handler.AuthSuccessDataHandler;
import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.core.service.AuthCache;
import com.gc.auth.extensions.jwt.handler.JwtAuthSuccessDataHandler;
import com.gc.auth.extensions.jwt.service.JwtService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author ShiZhongMing
 * 2021/1/8 15:59
 * @since 1.0
 */
@Configuration
@AutoConfigureBefore(name = "AuthSecurity2AutoConfiguration")
@AutoConfigureAfter(name = "AuthSaml2BeanConfiguration")
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

    /**
     * 创建 LogoutSuccessHandler
     * @return LogoutSuccessHandler
     */
    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new AuthLogoutSuccessHandler();
    }

    /**
     * 创建 RestAuthenticationProvider
     * @param userDetailsService userDetailsService
     * @return RestAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationProvider.class)
    public AuthenticationProvider restAuthenticationProvider(UserDetailsService userDetailsService) {
        return new RestAuthenticationProvider(userDetailsService);
    }

    /**
     * 创建 AuthSuccessDataHandler
     * @return JwtAuthSuccessDataHandler
     */
    @Bean
    @ConditionalOnMissingBean(JwtAuthSuccessDataHandler.class)
    public AuthSuccessDataHandler jwtAuthSuccessDataHandler() {
        return new JwtAuthSuccessDataHandler();
    }

}
