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
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
@EnableRedisHttpSession
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
    public void configure(WebSecurity web) throws Exception {
        WebSecurity and = web.ignoring().and();

        // 忽略 GET
        this.authProperties.getIgnores().getGet().forEach(url -> and.ignoring().antMatchers(HttpMethod.GET, url));

        // 忽略 POST
        this.authProperties.getIgnores().getPost().forEach(url -> and.ignoring().antMatchers(HttpMethod.POST, url));

        // 忽略 DELETE
        this.authProperties.getIgnores().getDelete().forEach(url -> and.ignoring().antMatchers(HttpMethod.DELETE, url));

        // 忽略 PUT
        this.authProperties.getIgnores().getPut().forEach(url -> and.ignoring().antMatchers(HttpMethod.PUT, url));

        // 忽略 HEAD
        this.authProperties.getIgnores().getHead().forEach(url -> and.ignoring().antMatchers(HttpMethod.HEAD, url));

        // 忽略 PATCH
        this.authProperties.getIgnores().getPatch().forEach(url -> and.ignoring().antMatchers(HttpMethod.PATCH, url));

        // 忽略 OPTIONS
        this.authProperties.getIgnores().getOptions().forEach(url -> and.ignoring().antMatchers(HttpMethod.OPTIONS, url));

        // 忽略 TRACE
        this.authProperties.getIgnores().getTrace().forEach(url -> and.ignoring().antMatchers(HttpMethod.TRACE, url));

        // 按照请求格式忽略
        this.authProperties.getIgnores().getPattern().forEach(url -> and.ignoring().antMatchers(url));

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        List<String> permitAllUrl = null;
//        if (StringUtils.isNotEmpty(this.authProperties.getPermitAll())) {
//            permitAllUrl = Arrays.stream(this.authProperties.getPermitAll().split(","))
//                    .map(String::trim)
//                    .filter(StringUtils :: isNotEmpty)
//                    .collect(Collectors.toList());
//        }

        http.cors()
                .and().csrf().disable().authorizeRequests()
                // 设置自定义投票器
//                .accessDecisionManager()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
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
                .logoutSuccessHandler(restLogoutSuccessHandler);


        if (this.authProperties.getDevelopment()) {
            http.cors().and().authorizeRequests().anyRequest().permitAll();
        } else {
            http.authorizeRequests()
                    .and();
//            if (ObjectUtils.isNotEmpty(permitAllUrl)) {
//                String[] matchers = new String[permitAllUrl.size()];
//                http.authorizeRequests().antMatchers(permitAllUrl.toArray(matchers)).permitAll().and();
//            }
            http.authorizeRequests()
                    // 其他请求全部拦截
                    .anyRequest().authenticated();
        }
    }

    /**
     * 创建投票器
     * @return
     */
//    private AccessDecisionManager createAccessDecisionManager() {
//        this.methodSecurityConfiguration.
//    }
}
