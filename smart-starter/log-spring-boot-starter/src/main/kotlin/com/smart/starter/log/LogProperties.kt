package com.smart.starter.log

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 日志配置
 * @author ming
 * 2019/6/28 下午4:00
 */
@ConfigurationProperties(prefix = "smart.log")
class LogProperties {
}