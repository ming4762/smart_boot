package com.smart.starter.fluorite.controller

import com.smart.common.message.Result
import com.smart.starter.fluorite.service.FluoriteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hikvision/token")
class TokenController {

    @Autowired
    private lateinit var fluoriteService: FluoriteService

    /**
     * 获取token
     */
    @RequestMapping("/get")
    fun get(): Result<String?> {
        return try {
            Result.success(this.fluoriteService.getToken())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}