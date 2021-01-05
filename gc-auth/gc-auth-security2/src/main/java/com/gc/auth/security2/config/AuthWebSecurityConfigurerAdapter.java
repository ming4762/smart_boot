package com.gc.auth.security2.config;

import com.gc.auth.core.handler.AuthAccessDeniedHandler;
import com.gc.auth.core.handler.RestAuthenticationEntryPoint;
import com.gc.auth.core.matcher.ExtensionPathMatcher;
import com.gc.auth.core.properties.AuthProperties;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 默认的web配置器
 * @author ShiZhongMing
 * 2021/1/5 8:46
 * @since 1.0
 */
public class AuthWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final AuthProperties authProperties;

    public AuthWebSecurityConfigurerAdapter(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public void configure(WebSecurity web) {
        WebSecurity and = web.ignoring().and();

        // 忽略 GET
        this.authProperties.getIgnores().getGet().forEach(url -> and.ignoring().requestMatchers(new ExtensionPathMatcher(HttpMethod.GET, url)));

        // 忽略 POST
        this.authProperties.getIgnores().getPost().forEach(url -> and.ignoring().requestMatchers(new ExtensionPathMatcher(HttpMethod.POST, url)));

        // 忽略 DELETE
        this.authProperties.getIgnores().getDelete().forEach(url -> and.ignoring().requestMatchers(new ExtensionPathMatcher(HttpMethod.DELETE, url)));

        // 忽略 PUT
        this.authProperties.getIgnores().getPut().forEach(url -> and.ignoring().requestMatchers(new ExtensionPathMatcher(HttpMethod.PUT, url)));

        // 忽略 HEAD
        this.authProperties.getIgnores().getHead().forEach(url -> and.ignoring().requestMatchers(new ExtensionPathMatcher(HttpMethod.HEAD, url)));

        // 忽略 PATCH
        this.authProperties.getIgnores().getPatch().forEach(url -> and.ignoring().requestMatchers(new ExtensionPathMatcher(HttpMethod.PATCH, url)));

        // 忽略 OPTIONS
        this.authProperties.getIgnores().getOptions().forEach(url -> and.ignoring().requestMatchers(new ExtensionPathMatcher(HttpMethod.OPTIONS, url)));

        // 忽略 TRACE
        this.authProperties.getIgnores().getTrace().forEach(url -> and.ignoring().requestMatchers(new ExtensionPathMatcher(HttpMethod.TRACE, url)));
        // 按照请求格式忽略
        this.authProperties.getIgnores().getPattern().forEach(url -> and.ignoring().requestMatchers(new ExtensionPathMatcher(url)));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(new AuthAccessDeniedHandler());
        // 开发模式不拦截
        if (BooleanUtils.isTrue(this.authProperties.getDevelopment())) {
            http.authorizeRequests().anyRequest().permitAll();
        }

    }
}
