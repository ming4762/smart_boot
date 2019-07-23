package com.smart.auth.controller

import com.smart.auth.model.vo.OnlineUserVO
import com.smart.auth.service.AuthService
import com.smart.common.message.Result
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    private lateinit var authService: AuthService

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

    /**
     * 获取在线用户
     */
    @PostMapping("auth/listOnlineUser")
    fun listOnlineUser(): Result<List<OnlineUserVO>?> {
        return try {
            Result.success(this.authService.listOnlineUser())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}