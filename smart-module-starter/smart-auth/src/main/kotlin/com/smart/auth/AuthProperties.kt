package com.smart.auth

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 配置项
 * @author ming
 * 2019/6/21 下午2:28
 */
@ConfigurationProperties(prefix = "smart.auth")
class AuthProperties {

    // session设置
    var session = Session()

    class Session {
        // 超时设置
        var timeout = Timeout()
    }

    /**
     * 超时参数
     */
    class Timeout {
        var global: Long = 3600

        var mobile: Long = 604800
    }
}