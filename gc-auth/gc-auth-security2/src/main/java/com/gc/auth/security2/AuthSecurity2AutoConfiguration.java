package com.gc.auth.security2;

import com.gc.auth.core.authentication.DefaultUrlAuthenticationProviderImpl;
import com.gc.auth.core.authentication.UrlAuthenticationProvider;
import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.core.service.AuthUserService;
import com.gc.auth.security2.service.DefaultUserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * AUTH 自动配置类
 * @author shizhongming
 * 2021/1/2 9:28 上午
 */
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthSecurity2AutoConfiguration {

    /**
     * 创建默认的 UserDetailsService
     * @param authUserService authUserService
     * @return UserDetailsService
     */
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public DefaultUserDetailsServiceImpl defaultUserDetailsService(AuthUserService authUserService) {
        return new DefaultUserDetailsServiceImpl(authUserService);
    }

    /**
     * 创建URL校验器
     * @param mapping springmvc映射
     * @return 动态URL校验器
     */
    @Bean
    @ConditionalOnProperty(prefix = "gc.auth", name = "urlCheck", havingValue = "true")
    @ConditionalOnMissingBean(UrlAuthenticationProvider.class)
    public UrlAuthenticationProvider urlAuthenticationProvider(RequestMappingHandlerMapping mapping, AuthProperties authProperties) {
        return new DefaultUrlAuthenticationProviderImpl(mapping, authProperties);
    }
}
