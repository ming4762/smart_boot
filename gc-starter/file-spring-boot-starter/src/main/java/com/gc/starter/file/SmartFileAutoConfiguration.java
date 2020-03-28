package com.gc.starter.file;

import com.gc.common.base.exception.BaseException;
import com.gc.starter.file.serice.ActualFileService;
import com.gc.starter.file.serice.impl.ActualFileServiceDiskImpl;
import com.gc.starter.file.serice.impl.ActualFileServiceMongoImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * 文件自动配置类
 * @author shizhongming
 * 2020/1/15 9:05 下午
 */
@Configuration
@EnableConfigurationProperties(SmartFileProperties.class)
public class SmartFileAutoConfiguration {


    /**
     * 创建文件执行器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ActualFileService.class)
    public ActualFileService actualFileService(SmartFileProperties properties) {
        String type = properties.getActuatorType();
        if (ActuatorTypeEnum.MONGO_DB.name().equals(type)) {
            return new ActualFileServiceMongoImpl();
        } else {
            if (StringUtils.isEmpty(properties.getBasePath())) {
                throw new BaseException("使用本地文件系统必须设置基础路径:smart.file.base-path");
            }
            return new ActualFileServiceDiskImpl(properties.getBasePath());
        }
    }
}
