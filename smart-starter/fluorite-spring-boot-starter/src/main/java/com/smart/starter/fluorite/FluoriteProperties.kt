package com.smart.starter.fluorite

import org.springframework.boot.context.properties.ConfigurationProperties


/**
 * 海康配置信息
 */
@ConfigurationProperties(prefix = "smart.fluorite")
class FluoriteProperties {

    var appKey: String? = null

    var appSecret: String? = null

    var retryCount: Int = 0
}