package com.gc.auth.extensions.jwt.config;

import com.gc.auth.core.handler.AuthAccessDeniedHandler;
import com.gc.auth.core.handler.AuthHandlerBuilder;
import com.gc.auth.core.handler.RestAuthenticationEntryPoint;
import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.core.service.AuthCache;
import com.gc.auth.extensions.jwt.context.JwtContext;
import com.gc.auth.extensions.jwt.filter.JwtAuthenticationFilter;
import com.gc.auth.extensions.jwt.filter.JwtLoginFilter;
import com.gc.auth.extensions.jwt.filter.JwtLogoutFilter;
import com.gc.auth.extensions.jwt.service.JwtService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JWT配置类
 * @author ShiZhongMing
 * 2020/12/31 14:58
 * @since 1.0
 */
@Slf4j
public class AuthJwtSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private final AuthHandlerBuilder handlerBuilder = new AuthHandlerBuilder();

    private JwtService jwtService;

    private JwtContext jwtContext;

    private final ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
        @Override
        public <O> O postProcess(O object) {
            return object;
        }
    };

    private AuthJwtSecurityConfigurer() {}

    /**
     * jwt 初始化
     * @return jwt
     */
    public static AuthJwtSecurityConfigurer jwt() {
        return new AuthJwtSecurityConfigurer();
    }

    /**
     * 初始化bean
     */
    private void initBean() {
        if (Objects.isNull(this.getAuthProperties())) {
            Assert.notNull(this.serviceProvider.authProperties, "properties is null, please init properties");
        }
        if (Objects.isNull(this.getAuthCache())) {
            Assert.notNull(this.serviceProvider.authCache, "auth cache is null, please init it");
        }
        if (Objects.isNull(this.getBean(JwtService.class, this.jwtService))) {
            this.jwtService = new JwtService(this.getAuthProperties(), this.getAuthCache());
        }

    }

    private AuthProperties getAuthProperties() {
        return this.getBean(AuthProperties.class, this.serviceProvider.authProperties);
    }

    private AuthCache<String, Object> getAuthCache() {
        return this.getBean(AuthCache.class, this.serviceProvider.authCache);
    }

    /**
     * 初始化函数
     * @param builder HttpSecurity
     * @throws Exception Exception
     */
    @Override
    public void init(HttpSecurity builder) throws Exception {
        // 初始化bean
        this.initBean();
        // 创建上下文
        this.jwtContext = this.createJwtContext();
        // 构建
        builder
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 设置异常信息拦截
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(new AuthAccessDeniedHandler())
                .and()
                // 添加登录 等处过滤器
                .addFilterAfter(this.createJwtFilterChainProxy(), BasicAuthenticationFilter.class)
                // 添加认证过滤器
                .addFilterAfter(new JwtAuthenticationFilter(this.jwtService, this.jwtContext), ExceptionTranslationFilter.class);

    }

    /**
     * 创建jwt 拦截器链
     * @return 拦截器链
     * @throws Exception Exception
     */
    private FilterChainProxy createJwtFilterChainProxy() throws Exception {
        final List<SecurityFilterChain> chains = Lists.newArrayList();
        // 创建登录过滤器
        final JwtLoginFilter jwtLoginFilter = this.jwtLoginFilter();
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(JwtLoginFilter.getLoginUrl(this.jwtContext)), jwtLoginFilter));

        // 创建logout过滤器
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getLogoutUrl()), this.jwtLogoutFilter()));

        return new FilterChainProxy(chains);
    }

    /**
     * 创建登录过滤器
     * @return 登录过滤器
     * @throws Exception Exception
     */
    private JwtLoginFilter jwtLoginFilter() throws Exception {
        final JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(this.jwtContext, this.jwtService);
        jwtLoginFilter.setFilterProcessesUrl(JwtLoginFilter.getLoginUrl(this.jwtContext));
        AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
        authenticationManagerBuilder.authenticationProvider(this.getBean(AuthenticationProvider.class, this.serviceProvider.authenticationProvider));
        jwtLoginFilter.setAuthenticationManager(authenticationManagerBuilder.build());
        // 设置登录成功handler
        jwtLoginFilter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class, this.handlerBuilder.getAuthenticationSuccessHandler()));
        // 设置登录失败handler
        jwtLoginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class, this.handlerBuilder.getAuthenticationFailureHandler()));
        return jwtLoginFilter;
    }

    /**
     * 创建登出过滤器
     * @return 登出过滤器
     */
    private JwtLogoutFilter jwtLogoutFilter() {
        // 创建LogoutHandler
        List<LogoutHandler> logoutHandlerList = this.handlerBuilder.getLogoutHandlers();
        if (CollectionUtils.isEmpty(logoutHandlerList)) {
            logoutHandlerList = Lists.newArrayList(this.getBean(LogoutHandler.class, this.jwtService));
        }
        final JwtLogoutFilter logoutFilter = new JwtLogoutFilter(this.getBean(LogoutSuccessHandler.class, this.handlerBuilder.getLogoutSuccessHandler()), logoutHandlerList.toArray(new LogoutHandler[]{}));
        logoutFilter.setFilterProcessesUrl(this.getLogoutUrl());
        return logoutFilter;
    }

    /**
     * 获取登出地址
     * @return 登出地址
     */
    protected String getLogoutUrl() {
        return Optional.ofNullable(this.serviceProvider.logoutUrl).orElse(JwtLogoutFilter.LOGOUT_URL);
    }

    /**
     * 获取服务配置类
     * @return 服务配置类
     */
    public ServiceProvider serviceProvider() {
        return this.serviceProvider;
    }

    /**
     * 创建handler Builder
     * @return handler Builder
     */
    public AuthHandlerBuilder authHandlerBuilder() {
        return this.handlerBuilder;
    }

    /**
     * 创建JWT 上下文
     * @return JWT上下文
     */
    private JwtContext createJwtContext() {
        return JwtContext.builder()
                .loginUrl(this.serviceProvider.loginUrl)
                .logoutUrl(this.serviceProvider.logoutUrl)
                .authProperties(this.getBean(AuthProperties.class, this.serviceProvider.authProperties))
                .build();
    }

    private <T> T getBean(Class<T> clazz, T t) {
        if (Objects.nonNull(t)) {
            return t;
        }
        try {
            return Optional.ofNullable(this.serviceProvider.applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("未找到Bean: " + e.getMessage());
            return null;
        }
    }
    /**
     * 服务配置类
     */
    public class ServiceProvider {
        private AuthProperties authProperties;

        private AuthCache<String, Object> authCache;

        private ApplicationContext applicationContext;

        private String loginUrl;

        private String logoutUrl;

        private AuthenticationProvider authenticationProvider;

        public AuthJwtSecurityConfigurer and() {
            return AuthJwtSecurityConfigurer.this;
        }

        public ServiceProvider applicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        public ServiceProvider authenticationProvider(AuthenticationProvider authenticationProvider) {
            this.authenticationProvider = authenticationProvider;
            return this;
        }

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
