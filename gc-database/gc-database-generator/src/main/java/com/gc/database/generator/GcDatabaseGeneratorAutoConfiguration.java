package com.gc.database.generator;

import com.gc.database.generator.engine.FreemarkerTemplateEngine;
import freemarker.cache.ByteArrayTemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/7/3 9:06 上午
 */
@Configuration
@ComponentScan
public class GcDatabaseGeneratorAutoConfiguration {

    /**
     * 创建模板引擎
     * TODO: 使用配置指定加载的引擎
     * @param byteArrayTemplateLoader 模板加载器
     * @param configuration freemarker配置
     * @return FreemarkerTemplateEngine
     */
    @Bean
    public FreemarkerTemplateEngine freemarkerTemplateEngine(ByteArrayTemplateLoader byteArrayTemplateLoader, freemarker.template.Configuration configuration) {
        return new FreemarkerTemplateEngine(byteArrayTemplateLoader, configuration);
    }
}
