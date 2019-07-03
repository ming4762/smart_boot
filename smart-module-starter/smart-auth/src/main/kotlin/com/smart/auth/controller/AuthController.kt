package com.smart.auth.controller

import com.smart.common.message.Result
import org.apache.shiro.SecurityUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/7/2 下午7:33
 */
@RestController
@RequestMapping
class AuthController {

    /**
     * 登出接口
     */
    @PostMapping("/logout")
    fun logout(): Result<Boolean?> {
        return try {
            SecurityUtils.getSubject().logout()
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}