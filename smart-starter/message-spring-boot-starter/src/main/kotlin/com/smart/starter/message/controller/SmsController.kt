package com.smart.starter.message.controller

import com.smart.common.message.Result
import com.smart.starter.message.model.SmsSendModel
import com.smart.starter.message.service.SmsService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 短信controller
 * @author ming
 * 2019/7/18 下午8:35
 */
@RequestMapping("/message/sms")
@RestController
class SmsController {

    @Autowired
    private lateinit var smsService: SmsService

    /**
     * 发送短信
     */
    @RequestMapping("/send")
    @RequiresPermissions("message:sms:send")
    fun send(@RequestBody model: SmsSendModel): Result<Boolean?> {
        return try {
            Result.success(this.smsService.send(model))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 批量发送短信
     */
    @RequestMapping("/batchSend")
    @RequiresPermissions("message:sms:send")
    fun batchSend(@RequestBody smsList: List<SmsSendModel>): Result<Boolean?> {
        return try {
            Result.success(this.smsService.batchSend(smsList))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}