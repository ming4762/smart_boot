package com.gc.starter.file.disk;

import com.gc.starter.file.disk.spring.DiskFileImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author shizhongming
 * 2020/11/5 10:52 下午
 */
@Configuration
@Import(DiskFileImportBeanDefinitionRegistrar.class)
public class SmartDiskFileAutoConfiguration {

//    @Bean(name = ActualFileServiceName.DISK_ACTUAL_FILE_SERVICE)
//    public ActualFileService actualFileService(SmartFileProperties properties) {
//        if (StringUtils.isBlank(properties.getBasePath())) {
//            // TODO: i18n
//            throw new BaseException("使用本地文件系统必须设置基础路径:gc.file.base-path");
//        }
//        return new ActualFileDiskServiceImpl(properties.getBasePath());
//    }
}
