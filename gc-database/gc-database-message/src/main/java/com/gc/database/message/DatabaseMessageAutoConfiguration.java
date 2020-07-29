package com.gc.database.message;

import com.gc.database.message.converter.ConverterInitializer;
import com.gc.database.message.converter.ConverterProvider;
import com.gc.database.message.converter.DefaultConverterProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/7/29 8:45 下午
 */
@Configuration
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
}
