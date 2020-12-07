package com.gc.starter.file.nfs;

import com.gc.file.common.properties.SmartFileProperties;
import com.gc.starter.file.nfs.provider.FtpChannelProvider;
import com.gc.starter.file.nfs.spring.FileNfsImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ShiZhongMing
 * 2020/12/7 14:54
 * @since 1.0
 */
@Configuration
@Import(FileNfsImportBeanDefinitionRegistrar.class)
public class SmartNfsFileAutoConfiguration {


    @Bean
    public FtpChannelProvider ftpChannelProvider(SmartFileProperties properties) {
        return new FtpChannelProvider(properties.getNfs());
    }
}
