package com.smart.starter.message

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.profile.DefaultProfile
import com.smart.starter.message.service.SmsService
import com.smart.starter.message.service.impl.AliyunSmsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.util.StringUtils

/**
 * 自动配置类
 * @author ming
 * 2019/7/18 下午2:35
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(MessageProperties :: class)
class MessageAutoConfiguration {

    @Autowired
    private lateinit var messageProperties: MessageProperties

    @Bean
    fun smsService(): SmsService {
        if (!StringUtils.isEmpty(this.messageProperties.sms.aliyun.id)) {
            return this.createAliyunSms()
        }
        // TODO:创建其他
        return this.createAliyunSms()
    }

    /**
     * 创建阿里云短信服务
     */
    private fun createAliyunSms(): SmsService {
        val aliyunSms = this.messageProperties.sms.aliyun
        val profile = DefaultProfile.getProfile(regionId, aliyunSms.id, aliyunSms.secret)
        DefaultProfile.addEndpoint(regionId, regionId, product, domain)
        val client = DefaultAcsClient(profile)
        return AliyunSmsServiceImpl(client)
    }


    companion object {
        //产品名称:云通信短信API产品
        private const val product = "Dysmsapi"
        //产品域名
        private const val domain = "dysmsapi.aliyuncs.com"
        private const val regionId = "cn-hangzhou"
    }
}