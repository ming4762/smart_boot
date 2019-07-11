package com.smart.starter.kettle

import com.smart.kettle.common.config.DatabaseMetaProperties
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 *
 * @author ming
 * 2019/7/11 上午9:43
 */
@ConfigurationProperties(prefix = "smart.kettle")
class KettleProperties {
    /**
     * 资源库配置
     */
    val repository = DatabaseMetaProperties()

}