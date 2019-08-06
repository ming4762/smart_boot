package com.smart.starter.fluorite.controller

import com.smart.common.message.Result
import com.smart.starter.fluorite.service.LiveService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 直播API接口
 */
@RestController
@RequestMapping("/fluorite/live")
class LiveController {


    @Autowired
    private lateinit var liveService: LiveService

    /**
     * 获取用户下直播视频列表
     */
    @RequestMapping("/videoList")
    fun videoList(parameter: Map<String, Int>): Result<Map<String, Any>?> {
        return try {
            Result.success(this.liveService.videoList(parameter["startPage"], parameter["pageSize"]))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}