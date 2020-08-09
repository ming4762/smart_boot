package com.gc.database.message;

import com.gc.common.base.imports.EnableSpringContext;
import com.gc.database.message.converter.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/7/29 8:45 下午
 */
@Configuration
@ComponentScan
@EnableSpringContext
public class DatabaseMessageAutoConfiguration {

    /**
     * 创建默认的 converterProvider
     * @return converterProvider
     */
    @Bean
    @ConditionalOnMissingBean(ConverterProvider.class)
    public ConverterProvider converterProvider() {
        return new DefaultConverterProvider();
    }

    @Bean
    public ConverterInitializer converterInitializer(ConverterProvider converterProvider) {
        return new ConverterInitializer(converterProvider);
    }

    /**
     * 创建默认的DbJavaTypeConverter
     * @return DbJavaTypeConverter
     */
    @Bean
    @ConditionalOnMissingBean(DbJavaTypeConverter.class)
    public DbJavaTypeConverter dbJavaTypeConverter() {
        return new DefaultDbJavaTypeConverter();
    }
}
