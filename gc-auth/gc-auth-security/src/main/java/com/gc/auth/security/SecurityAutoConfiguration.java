package com.gc.auth.security;

import com.gc.auth.security.authentication.RestAuthenticationProvider;
import com.gc.auth.security.handler.*;
import com.gc.common.auth.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * @author shizhongming
 * 2020/1/17 9:24 下午
 */
@Configuration
@ComponentScan
@EnableWebSecurity
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 300)
@EnableConfigurationProperties(AuthProperties.class)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestAuthAccessDeniedHandler restAuthAccessDeniedHandler;

    @Autowired
    private RestAuthSuccessHandler restAuthSuccessHandler;

    @Autowired
    private RestLogoutSuccessHandler restLogoutSuccessHandler;

    @Autowired
    private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

    @Autowired
    private RestAuthenticationProvider provider;

    /**
     * 创建session id 读取类
     * @return session id 读取
     */
    @Bean
    public HeaderHttpSessionIdResolver headerHttpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver(HttpHeaders.AUTHORIZATION);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 加入自定义的安全认证
        auth.authenticationProvider(provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (this.authProperties.getDevelopment()) {
            http.authorizeRequests().antMatchers().permitAll().and().cors().and().csrf().disable();
        } else {
            http.cors()
                    .and().authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                    .antMatchers("/public/**")
                    .permitAll()
                    .antMatchers().hasRole("SUPERADMIN").anyRequest().permitAll()
                    // 其他请求全部拦截
                    .anyRequest().authenticated()
                    .and()
                    // 未登录处理
                    .httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint)
                    .and()
                    // 权限不足
                    .exceptionHandling().accessDeniedHandler(restAuthAccessDeniedHandler)
                    .and()
                    .formLogin()
                    .loginProcessingUrl("/public/auth/login")
                    // 登录成功
                    .successHandler(restAuthSuccessHandler)
                    //配置登录失败的自定义处理类
                    .failureHandler(restAuthenticationFailureHandler)
                    .and()
                    .logout()
                    .logoutUrl("/public/auth/logout")
                    // 登出成功
                    .logoutSuccessHandler(restLogoutSuccessHandler)
                    .and().csrf().disable();
        }
    }
}
