package com.gc.starter.log;

import com.gc.common.base.constants.SqlSessionTemplateConstants;
import com.gc.starter.log.aspect.LogAspect;
import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan(value = "com.gc.starter.log.mapper", sqlSessionTemplateRef = SqlSessionTemplateConstants.SYSTEM_TEMPLATE)
public class GcLogAutoConfiguration {

    /**
     * 创建日志切面
     * @return
     */
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
