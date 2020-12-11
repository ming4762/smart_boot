package com.gc.module.file;

import com.gc.file.common.properties.SmartFileProperties;
import com.gc.module.file.service.SysFileService;
import com.gc.module.file.service.impl.DefaultFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
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

    /**
     * 创建文件服务实体类
     * @param applicationContext spring上下文
     * @param properties 参数
     * @return 文件服务实体类
     */
    @Bean
    @ConditionalOnMissingBean(SysFileService.class)
    public SysFileService sysFileService(ApplicationContext applicationContext, SmartFileProperties properties) {
        return new DefaultFileServiceImpl(applicationContext, properties);
    }
}
