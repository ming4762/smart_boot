package com.smart.system.controller

import com.smart.common.message.Result
import com.smart.system.config.LocalConfigProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/8/13 上午8:50
 */
@RestController
@RequestMapping("public/sys")
class SystemPublicController {

    @Autowired(required = false)
    private var localConfigProperties: LocalConfigProperties? = null

    /**
     * 读取本地配置信息
     */
    @RequestMapping("/readConfig")
    fun readConfig (): Result<Any?> {
        return try {
            Result.success(this.localConfigProperties?.config)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}