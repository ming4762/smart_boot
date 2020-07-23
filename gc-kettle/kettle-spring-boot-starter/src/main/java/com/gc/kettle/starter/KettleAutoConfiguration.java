package com.gc.kettle.starter;

import com.gc.kettle.starter.pool.KettleRepositoryProvider;
import com.gc.kettle.starter.pool.impl.DefaultKettleRepositoryProviderImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/7/16 8:10 下午
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(KettleProperties.class)
public class KettleAutoConfiguration {

    /**
     * 创建资源库提供器
     * @param kettleProperties 配置信息
     * @return 资源库提供器
     */
    @Bean
    @ConditionalOnMissingBean(KettleRepositoryProvider.class)
    public KettleRepositoryProvider kettleRepositoryProvider(KettleProperties kettleProperties) {
        return new DefaultKettleRepositoryProviderImpl(kettleProperties.getRepository());
    }

}
