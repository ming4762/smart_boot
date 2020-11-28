package com.gc.module.file;

import com.gc.file.common.properties.SmartFileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author jacksons
 * 2020/1/27 12:37 下午
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(SmartFileProperties.class)
public class ModuleFileAutoConfiguration {
}
