package com.gc.starter.file.mongo;

import com.gc.starter.file.mongo.spring.MongoFileImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author shizhongming
 * 2020/11/14 10:41 下午
 */
@Configuration
@Import(MongoFileImportBeanDefinitionRegistrar.class)
public class SmartMongoFileAutoConfiguration {

}
