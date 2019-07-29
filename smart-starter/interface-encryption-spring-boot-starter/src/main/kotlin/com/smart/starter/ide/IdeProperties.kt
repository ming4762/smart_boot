package com.smart.starter.ide

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 *
 * @author ming
 * 2019/7/24 下午4:56
 */
@ConfigurationProperties(prefix = "smart.auth.ide")
class IdeProperties {
    // 是否启用接口加密
    var enable: Boolean = false
    // 接口有效期，默认5分钟
    var timeDiff = 5 * 60 * 1000
}