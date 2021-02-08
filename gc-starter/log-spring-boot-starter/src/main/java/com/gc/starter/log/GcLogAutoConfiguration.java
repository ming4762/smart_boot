package com.gc.starter.log;

import com.gc.starter.log.aspect.LogAspect;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志配置类
 * @author jackson
 * 2020/1/22 1:52 下午
 */
@Configuration
@EnableConfigurationProperties(LogProperties.class)
public class GcLogAutoConfiguration {

    /**
     * 创建日志切面
     * @return 日志切面
     */
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
