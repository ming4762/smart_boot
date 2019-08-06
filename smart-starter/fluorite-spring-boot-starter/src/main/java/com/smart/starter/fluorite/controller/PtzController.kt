package com.smart.starter.fluorite.controller

import com.smart.common.message.Result
import com.smart.starter.fluorite.model.po.PtzPO
import com.smart.starter.fluorite.service.PtzService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hikvision/ptz")
class PtzController {

    @Autowired
    private lateinit var ptzService: PtzService

    /**
     * 开始云台控制
     */
    @RequestMapping("/start")
    fun start(@RequestBody parameter: PtzPO): Result<Boolean?> {
        return try {
            this.ptzService.start(parameter)
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message, false)
        }
    }

    /**
     * 结束云台控制
     */
    @RequestMapping("/stop")
    fun stop(@RequestBody parameter: PtzPO): Result<Boolean?> {
        return try {
            this.ptzService.stop(parameter)
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message, false)
        }
    }
}