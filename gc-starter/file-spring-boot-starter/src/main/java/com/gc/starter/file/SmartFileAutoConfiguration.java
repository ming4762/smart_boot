package com.gc.starter.file;

import com.gc.starter.file.serice.ActualFileService;
import com.gc.starter.file.serice.impl.ActualFileServiceDiskImpl;
import com.gc.starter.file.serice.impl.ActualFileServiceMongoImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SmartFileProperties properties;

    /**
     * 创建文件执行器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ActualFileService.class)
    public ActualFileService actualFileService() throws Exception {
        String type = this.properties.getActuatorType();
        if (ActuatorTypeEnum.mongoDB.name().equals(type)) {
            return new ActualFileServiceMongoImpl();
        } else {
            if (StringUtils.isEmpty(this.properties.getBasePath())) {
                throw new Exception("使用本地文件系统必须设置基础路径:smart.file.base-path");
            }
            return new ActualFileServiceDiskImpl(this.properties.getBasePath());
        }
    }
}
