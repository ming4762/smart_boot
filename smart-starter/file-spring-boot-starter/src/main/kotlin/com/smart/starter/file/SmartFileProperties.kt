package com.smart.starter.file

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 文件配置类
 * @author ming
 * 2019/6/14 下午8:56
 */
@ConfigurationProperties(prefix = "smart.file")
class SmartFileProperties {
    // 执行器类型
    var actuatorType = ActuatorTypeEnum.mongoDB.name

    var basePath: String? = null
}