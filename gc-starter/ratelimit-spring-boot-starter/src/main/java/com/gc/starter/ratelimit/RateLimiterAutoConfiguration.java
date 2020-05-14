package com.gc.starter.ratelimit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/4/16 2:11 下午
 */
@EnableConfigurationProperties(RateLimiterProperties.class)
@Configuration
@ComponentScan
public class RateLimiterAutoConfiguration {
}
