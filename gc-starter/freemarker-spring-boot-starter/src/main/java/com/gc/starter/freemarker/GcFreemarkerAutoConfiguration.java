package com.gc.starter.freemarker;

import freemarker.cache.ByteArrayTemplateLoader;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Collection;

/**
 * @author shizhongming
 * 2020/7/2 8:27 下午
 */
@Configuration
@EnableConfigurationProperties(FreemarkerProperties.class)
public class GcFreemarkerAutoConfiguration {

    /**
     * 创建freemarker配置
     * @param properties 配置参数
     * @return freemarker配置
     */
    @Bean
    public freemarker.template.Configuration configuration(FreemarkerProperties properties, ApplicationContext applicationContext) {
        final FreemarkerProperties.Config config = properties.getConfig();
        // 创建freemarker配置
        final freemarker.template.Configuration configuration = new freemarker.template.Configuration(config.getVersion());
        configuration.setDefaultEncoding(config.getEncoding().name());
        // 设置加载器
        Collection<TemplateLoader> loaders = applicationContext.getBeansOfType(TemplateLoader.class).values();
        configuration.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[0])));
        return configuration;
    }

    /**
     * 创建ByteArrayTemplateLoader
     * @return ByteArrayTemplateLoader
     */
    @Bean
    @Order(Integer.MIN_VALUE)
    @ConditionalOnMissingBean(ByteArrayTemplateLoader.class)
    public ByteArrayTemplateLoader byteArrayTemplateLoader() {
        return new ByteArrayTemplateLoader();
    }

    /**
     * 创建ClassTemplateLoader
     * @return ClassTemplateLoader
     */
    @Bean
    @Order(Integer.MIN_VALUE + 1)
    @ConditionalOnMissingBean(ClassTemplateLoader.class)
    public ClassTemplateLoader classTemplateLoader() {
        return new ClassTemplateLoader(this.getClass().getClassLoader(), "/");
    }
}
