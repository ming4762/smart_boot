package com.gc.auth.security;

import com.gc.auth.security.authentication.DynamicUrlCheckProvider;
import com.gc.auth.security.service.RestUserDetailsServiceImpl;
import com.gc.common.auth.properties.AuthProperties;
import com.gc.common.auth.service.AuthUserService;
import com.google.common.collect.ImmutableList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 认证模块自动配置类
 * @author shizhongming
 * 2020/1/17 9:24 下午
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(AuthProperties.class)
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService createUserDetailsService(AuthUserService userService) {
        return new RestUserDetailsServiceImpl(userService);
    }

    /**
     * 创建session id 读取类
     * @return session id 读取
     */
//    @Bean
//    public HeaderHttpSessionIdResolver headerHttpSessionIdResolver() {
//        return new HeaderHttpSessionIdResolver(HttpHeaders.AUTHORIZATION);
//    }

    /**
     * cors资源配置
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(ImmutableList.of("authority", "Authorization", "Origin", "No-Cache", "X-Requested-With", "If-Modified-Since", "Pragma", "Last-Modified", "Cache-Control", "Expires", "Content-Type", "X-E4M-With", "token", "Content-Disposition"));
        corsConfiguration.setAllowedOrigins(ImmutableList.of("*"));
        corsConfiguration.setAllowedMethods(ImmutableList.of("POST", "GET", "PUT", "OPTIONS", "DELETE", "PATCH"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * 创建URL校验器
     * @param mapping
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "gc.auth", name = "urlCheck", havingValue = "true")
    public DynamicUrlCheckProvider dynamicUrlCheckProvider(RequestMappingHandlerMapping mapping, AuthProperties authProperties) {
        return new DynamicUrlCheckProvider(mapping, authProperties);
    }

}
