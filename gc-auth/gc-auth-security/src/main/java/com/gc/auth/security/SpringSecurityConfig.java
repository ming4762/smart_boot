package com.gc.auth.security;

import com.gc.auth.security.authentication.RestAuthenticationProvider;
import com.gc.auth.security.filter.JwtAuthenticationFilter;
import com.gc.auth.security.handler.*;
import com.gc.auth.security.matcher.ExtensionPathMatcher;
import com.gc.common.auth.properties.AuthProperties;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security 配置
 * @author shizhongming
 * 2020/4/12 10:51 上午
 */
@EnableWebSecurity
@Configuration
@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProperties authProperties;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final RestAuthAccessDeniedHandler restAuthAccessDeniedHandler;

    private final RestAuthSuccessHandler restAuthSuccessHandler;

    private final RestLogoutSuccessHandler restLogoutSuccessHandler;

    private final RestAuthenticationFailureHandler restAuthenticationFailureHandler;

    private final RestAuthenticationProvider provider;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SpringSecurityConfig(AuthProperties authProperties, RestAuthenticationEntryPoint restAuthenticationEntryPoint, RestAuthAccessDeniedHandler restAuthAccessDeniedHandler, RestAuthSuccessHandler restAuthSuccessHandler, RestLogoutSuccessHandler restLogoutSuccessHandler, RestAuthenticationFailureHandler restAuthenticationFailureHandler, RestAuthenticationProvider provider) {
        this.authProperties = authProperties;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restAuthAccessDeniedHandler = restAuthAccessDeniedHandler;
        this.restAuthSuccessHandler = restAuthSuccessHandler;
        this.restLogoutSuccessHandler = restLogoutSuccessHandler;
        this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
        this.provider = provider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // 加入自定义的安全认证
        auth.authenticationProvider(provider);
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

        http.cors()
                .and().csrf().disable().authorizeRequests()
                // 设置自定义投票器
//                .accessDecisionManager()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .and()
                // 权限不足
                .exceptionHandling().accessDeniedHandler(restAuthAccessDeniedHandler);

        if (this.authProperties.isJwt()) {
            http.formLogin().disable()
                    .httpBasic().disable()
                    .logout().disable()
                    // Session 管理
                    .sessionManagement()
                    // 因为使用了JWT，所以这里不管理Session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            // 添加自定义 JWT 过滤器
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        } else {
            // 未登录处理
            http.formLogin()
                    .loginProcessingUrl("/auth/login")
                    // 登录成功
                    .successHandler(restAuthSuccessHandler)
                    //配置登录失败的自定义处理类
                    .failureHandler(restAuthenticationFailureHandler)
                    .and().httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint)
                    .and()
                    .logout()
                    .logoutUrl("/auth/logout")
                    // 登出成功
                    .logoutSuccessHandler(restLogoutSuccessHandler);
        }

        if (BooleanUtils.isTrue(this.authProperties.getDevelopment())) {
            http.cors().and().authorizeRequests().anyRequest().permitAll();
        } else {
            if (this.authProperties.isUrlCheck()) {
                http.authorizeRequests()
                        .anyRequest()
                        .access("@dynamicUrlCheckProvider.hasUrlPermission(request, authentication)");
            } else {
                http.authorizeRequests()
                        // 其他请求全部拦截
                        .anyRequest().authenticated();
            }
        }
    }

    @Autowired(required = false)
    public void setJwtAuthenticationFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
}
