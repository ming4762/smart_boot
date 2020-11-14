package com.gc.starter.file.mongo;

import com.gc.file.common.service.ActualFileService;
import com.gc.starter.file.mongo.service.ActualFileServiceMongoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * @author shizhongming
 * 2020/11/14 10:41 下午
 */
@Configuration
public class SmartMongoFileAutoConfiguration {

    @Bean
    public ActualFileService actualFileService(@Autowired GridFsTemplate gridFsTemplate, @Autowired MongoDatabaseFactory dbFactory) {
        return new ActualFileServiceMongoImpl(gridFsTemplate, dbFactory);
    }
}
