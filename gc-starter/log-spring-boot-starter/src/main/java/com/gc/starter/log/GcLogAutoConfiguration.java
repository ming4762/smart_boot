package com.gc.starter.log;

import com.gc.starter.log.aspect.LogAspect;
import com.gc.starter.log.handler.DefaultLogHandler;
import com.gc.starter.log.handler.LogHandler;
import com.gc.starter.log.service.SysLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 日志配置类
 * @author jackson
 * 2020/1/22 1:52 下午
 */
@ComponentScan
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

    /**
     * 创建默认的日志执行器
     * @param sysLogService 系统日志服务层
     * @return LogHandler
     */
    @Bean
    @ConditionalOnMissingBean(LogHandler.class)
    public LogHandler logHandler(SysLogService sysLogService) {
        return new DefaultLogHandler(sysLogService);
    }
}
