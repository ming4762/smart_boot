package com.gc.common.base.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author jackson
 * 2020/2/16 3:27 下午
 */
@Configuration
public class CorsConfiguration {

    @Bean("corsFilterRegistrationBean")
    @ConditionalOnMissingBean(name = "corsFilterRegistrationBean")
    public FilterRegistrationBean<Filter> corsFilterRegistrationBean() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("corsFilterRegistrationBean");
        registration.setOrder(0);
        return registration;
    }
}
