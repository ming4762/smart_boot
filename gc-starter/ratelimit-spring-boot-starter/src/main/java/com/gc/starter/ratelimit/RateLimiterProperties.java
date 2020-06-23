package com.gc.starter.ratelimit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 限流参数
 * @author shizhongming
 * 2020/4/16 2:08 下午
 */
@ConfigurationProperties(prefix = "gc.limit")
@Getter
@Setter
public class RateLimiterProperties {

    /**
     * 是否启用全局限流
     */
    private Boolean global = Boolean.TRUE;

    /**
     * 全局限流最大值
     */
    private Long globalMax = 20L;

    private Long timeout = 1L;
}
