package com.smart.system.config

import com.smart.common.loader.YamlPropertyLoaderFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

/**
 * 本地化配置信息
 */
@Component
@ConfigurationProperties(prefix = "smart.local")
@ConditionalOnProperty(prefix = "smart.config", name = ["localPath"])
@PropertySource(value = ["classpath:\${smart.config.localPath}"], factory = YamlPropertyLoaderFactory ::class)
class LocalConfigProperties {

    var config: Map<String, Any> = mutableMapOf()
}