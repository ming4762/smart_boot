package com.smart.starter.message

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 *
 * @author ming
 * 2019/7/18 下午2:35
 */
@ConfigurationProperties(prefix = "smart.message")
class MessageProperties {


    var sms = Sms()

    /**
     * 短信配置
     */
    class Sms {
        var aliyun = AliyunSms()
    }

    class AliyunSms() {
        // APPID
        var id: String = ""
        // app secret
        var secret: String = ""
    }
}