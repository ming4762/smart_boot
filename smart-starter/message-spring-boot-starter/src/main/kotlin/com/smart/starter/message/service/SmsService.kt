package com.smart.starter.message.service

import com.smart.starter.message.model.SmsSendModel

/**
 * 短信服务层
 * @author ming
 * 2019/7/18 下午2:43
 */
interface SmsService {

    fun send(send: SmsSendModel): Boolean


    fun batchSend(sendList: List<SmsSendModel>): Boolean
}