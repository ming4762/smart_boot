package com.gc.starter.file.disk;

import com.gc.common.base.exception.BaseException;
import com.gc.file.common.constants.ActualFileServiceName;
import com.gc.file.common.properties.SmartFileProperties;
import com.gc.file.common.service.ActualFileService;
import com.gc.starter.file.disk.service.ActualFileDiskServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/11/5 10:52 下午
 */
@Configuration
public class SmartDiskFileAutoConfiguration {

    @Bean(name = ActualFileServiceName.DISK_ACTUAL_FILE_SERVICE)
    public ActualFileService actualFileService(SmartFileProperties properties) {
        if (StringUtils.isBlank(properties.getBasePath())) {
            // TODO: i18n
            throw new BaseException("使用本地文件系统必须设置基础路径:gc.file.base-path");
        }
        return new ActualFileDiskServiceImpl(properties.getBasePath());
    }
}
