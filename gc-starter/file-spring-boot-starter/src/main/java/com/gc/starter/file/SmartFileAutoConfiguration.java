package com.gc.starter.file;

import com.gc.common.base.exception.BaseException;
import com.gc.starter.file.serice.ActualFileService;
import com.gc.starter.file.serice.impl.ActualFileServiceDiskImpl;
import com.gc.starter.file.serice.impl.ActualFileServiceMongoImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * 文件自动配置类
 * @author shizhongming
 * 2020/1/15 9:05 下午
 */
@Configuration
@EnableConfigurationProperties(SmartFileProperties.class)
public class SmartFileAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(ActualFileService.class)
    public ActualFileService actualFileService(SmartFileProperties properties, @Autowired(required = false) GridFsTemplate gridFsTemplate, @Autowired(required = false) MongoDbFactory dbFactory) {
        if (StringUtils.equalsIgnoreCase(properties.getActuatorType(), ActuatorTypeEnum.LOCAL.name())) {
            if (org.springframework.util.StringUtils.isEmpty(properties.getBasePath())) {
                throw new BaseException("使用本地文件系统必须设置基础路径:gc.file.base-path");
            }
            return new ActualFileServiceDiskImpl(properties.getBasePath());
        } else {
            if (!ObjectUtils.allNotNull(gridFsTemplate, dbFactory)) {
                throw new BaseException("创建文件执行器失败，请检查是否引入mongoDB支持");
            }
            return new ActualFileServiceMongoImpl(gridFsTemplate, dbFactory);
        }
    }
}
