package com.gc.auth.extensions.saml2.config;

import com.google.common.collect.Lists;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.log.SAMLLogger;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import java.util.List;

/**
 * SAML2配置类
 * @author ShiZhongMing
 * 2021/1/6 10:26
 * @since 1.0
 */
public class AuthSaml2SecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private static AuthSaml2SecurityConfigurer authSaml2SecurityConfigurer = new AuthSaml2SecurityConfigurer();

    @Override
    public void init(HttpSecurity builder) throws Exception {
        Assert.notNull(this.serviceProvider.applicationContext, "applicationContext is null, please init it");
        // 创建 ExtendedMetadata
        builder
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/saml/**").permitAll();
        // 添加Filter
        builder.addFilterBefore(this.createMetadataGeneratorFilter(), ChannelProcessingFilter.class)
                .addFilterAfter(this.createSamlFilter(), BasicAuthenticationFilter.class);

        builder.logout().disable();
    }

    public static AuthSaml2SecurityConfigurer saml2() {
        return authSaml2SecurityConfigurer;
    }

    public ServiceProvider serviceProvider() {
        return this.serviceProvider;
    }

    /**
     * 创建 MetadataGeneratorFilter
     * @return MetadataGeneratorFilter
     */
    private MetadataGeneratorFilter createMetadataGeneratorFilter() {
        final MetadataGeneratorFilter filter = new MetadataGeneratorFilter(this.getBean(MetadataGenerator.class));
        filter.setManager(this.getBean(MetadataManager.class));
        return filter;
    }

    /**
     * 创建SAML过滤器链
     * @return
     */
    private FilterChainProxy createSamlFilter() {
        List<SecurityFilterChain> chains = Lists.newArrayList();
        // 登录过滤器
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/login/**"), this.getBean(SAMLEntryPoint.class)));

        // 登出过滤器
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/logout/**"), this.samlLogoutFilter()));

        return new FilterChainProxy(chains);
    }

    /**
     * 创建 SAMLLogoutFilter
     * @return SAMLLogoutFilter
     */
    private SAMLLogoutFilter samlLogoutFilter() {
        final SAMLLogoutFilter samlLogoutFilter =  new SAMLLogoutFilter(
                this.getBean(LogoutSuccessHandler.class),
                new LogoutHandler[]{this.getBean(LogoutHandler.class)},
                new LogoutHandler[]{this.getBean(LogoutHandler.class)}
                );
        samlLogoutFilter.setProfile(this.getBean(SingleLogoutProfile.class));
        samlLogoutFilter.setContextProvider(this.getBean(SAMLContextProvider.class));
        samlLogoutFilter.setSamlLogger(this.getBean(SAMLLogger.class));
        return samlLogoutFilter;
    }

    /**
     * 从容器中获取指定类型bean
     * @param clazz 类型
     * @param <T> 类型
     * @return bean实体
     */
    private <T> T getBean(Class<T> clazz) {
        return this.serviceProvider.applicationContext.getBean(clazz);
    }

    /**
     * 服务配置类
     */
    public class ServiceProvider {

        private ApplicationContext applicationContext;

        public ServiceProvider applicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        public AuthSaml2SecurityConfigurer and() {
            return AuthSaml2SecurityConfigurer.this;
        }

    }



}
