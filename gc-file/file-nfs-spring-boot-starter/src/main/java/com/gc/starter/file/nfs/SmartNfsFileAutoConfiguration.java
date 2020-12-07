package com.gc.starter.file.nfs;

import com.gc.file.common.properties.SmartFileProperties;
import com.gc.file.common.service.ActualFileService;
import com.gc.starter.file.nfs.provider.FtpChannelProvider;
import com.gc.starter.file.nfs.service.ActualFileServiceNfsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2020/12/7 14:54
 * @since 1.0
 */
@Configuration
public class SmartNfsFileAutoConfiguration {

    /**
     * 创建文件执行器
     * @return 文件执行器
     */
    @Bean(name = "ActualFileNfsService")
    public ActualFileService actualFileServiceNfsImpl(SmartFileProperties properties) {
        return new ActualFileServiceNfsImpl(this.ftpChannelProvider(properties));
    }

    @Bean
    public FtpChannelProvider ftpChannelProvider(SmartFileProperties properties) {
        return new FtpChannelProvider(properties.getNfs());
    }
}
