package com.smart.starter.message.service.impl

import com.alibaba.fastjson.JSON
import com.aliyuncs.IAcsClient
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest
import com.smart.starter.message.model.SmsSendModel
import com.smart.starter.message.service.SmsService

/**
 *
 * @author ming
 * 2019/7/18 下午2:44
 */
class AliyunSmsServiceImpl(private val acsClient: IAcsClient) : SmsService {
    /**
     * 发送短信
     */
    override fun send(send: SmsSendModel): Boolean {
        val request = this.createRequest(send)
        val response = this.acsClient.getAcsResponse(request)
        return response.code == "OK"
    }

    /**
     * 批量发送
     */
    override fun batchSend(sendList: List<SmsSendModel>): Boolean {
        val resultList = sendList.map { this.send(it) }
        return resultList.all { it }
    }


    /**
     * 创建发送请求
     */
    private fun createRequest(model: SmsSendModel): SendSmsRequest {
        val request = SendSmsRequest()
        // 创建号码
        val stringBuffer = StringBuffer()
        if (stringBuffer.endsWith(",")) {
            stringBuffer.deleteCharAt(stringBuffer.length - 1)
        }
        model.phoneNumberList?.forEach {
            stringBuffer.append(it).append(",")
        }
        if (stringBuffer.endsWith(",")) {
            stringBuffer.deleteCharAt(stringBuffer.length - 1)
        }
        // 设置号码
        request.phoneNumbers = stringBuffer.toString()
        request.signName = model.signName
        request.templateCode = model.templateCode

        request.templateParam = JSON.toJSONString(model.templateParam)
        return request
    }
}