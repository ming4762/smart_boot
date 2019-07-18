package com.smart.starter.message.model

import java.io.Serializable

/**
 * 短信发送实体类
 * @author ming
 * 2019/7/18 下午2:42
 */
class SmsSendModel : Serializable {

    var phoneNumberList: List<String>? = null
    var signName: String? = null
    var templateCode: String? = null
    var templateParam: Map<String, Any>? = null
}