package com.gc.auth.extensions.jwt.config;

import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.core.service.AuthCache;
import com.gc.auth.extensions.jwt.context.JwtContext;
import com.gc.auth.extensions.jwt.filter.JwtAuthenticationFilter;
import com.gc.auth.extensions.jwt.service.JwtService;
import lombok.Getter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.util.Assert;

/**
 * JWT配置类
 * @author ShiZhongMing
 * 2020/12/31 14:58
 * @since 1.0
 */
public class AuthJwtSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private JwtContext jwtContext;

    /**
     * 初始化函数
     * @param builder HttpSecurity
     * @throws Exception Exception
     */
    @Override
    public void init(HttpSecurity builder) throws Exception {
        Assert.notNull(this.serviceProvider.authProperties, "properties is null, please init properties");
        // 创建上下文
        this.jwtContext = this.createJwtContext();
        // 创建JWT服务
        final JwtService jwtService = new JwtService(this.serviceProvider.authProperties.getJwtKey(), this.serviceProvider.authCache);
        // 创建JWT认证Filter
        final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, this.serviceProvider.authProperties);
        // 创建登录过滤器
        super.init(builder);
    }

    /**
     * 获取服务配置类
     * @return 服务配置类
     */
    public ServiceProvider serviceProvider() {
        return this.serviceProvider;
    }

    /**
     * 创建JWT 上下文
     * @return JWT上下文
     */
    private JwtContext createJwtContext() {
        return JwtContext.builder()
                .loginUrl(this.serviceProvider.loginUrl)
                .logoutUrl(this.serviceProvider.logoutUrl)
                .build();
    }


    /**
     * 服务配置类
     */
    @Getter
    public static class ServiceProvider {
        private AuthProperties authProperties;

        private AuthCache<String, Object> authCache;

        private String loginUrl;

        private String logoutUrl;

        public ServiceProvider properties(AuthProperties authProperties) {
            this.authProperties = authProperties;
            return this;
        }

        public ServiceProvider authCache(AuthCache<String, Object> authCache) {
            this.authCache = authCache;
            return this;
        }

        public ServiceProvider loginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
            return this;
        }

        public ServiceProvider logoutUrl(String logoutUrl) {
            this.logoutUrl = logoutUrl;
            return this;
        }

    }


}
