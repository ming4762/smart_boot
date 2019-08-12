package com.smart.starter.file

import com.smart.starter.file.service.ActualFileService
import com.smart.starter.file.service.impl.ActualFileServiceDiskImpl
import com.smart.starter.file.service.impl.ActualFileServiceMongoImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 自动配置启动类
 * @author ming
 * 2019/6/14 下午8:43
 */
@Configuration
@EnableConfigurationProperties(SmartFileProperties :: class)
class SmartFileAutoConfiguration {

    @Autowired
    private lateinit var fileProperties: SmartFileProperties

    /**
     * 创建文件执行器
     */
    @Bean
    @ConditionalOnMissingBean(ActualFileService :: class)
    fun actualFileService(): ActualFileService {
        val type = fileProperties.actuatorType
        if (type == ActuatorTypeEnum.mongoDB.name) {
            return ActualFileServiceMongoImpl()
        } else {
            val basePath = fileProperties.basePath ?: throw Exception("使用本地文件系统必须设置基础路径")
            return ActualFileServiceDiskImpl(basePath)
        }
    }
}