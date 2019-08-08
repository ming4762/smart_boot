package com.smart.auth.controller

import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.smart.auth.common.constants.AuthConstants
import com.smart.auth.common.model.SysUserDO
import com.smart.auth.common.service.AuthUserService
import com.smart.auth.common.utils.AuthUtils
import com.smart.auth.model.po.UpdatePasswordPO
import com.smart.auth.model.vo.OnlineUserVO
import com.smart.auth.service.AuthService
import com.smart.common.log.annotation.Log
import com.smart.common.message.Result
import com.smart.starter.ide.util.RsaKeyUtils
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
     * 用户服务
     */
    @Autowired
    private lateinit var userService: AuthUserService

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
     * 验证token是否有效
     * @author zhongming（2019-08-01）
     */
    @PostMapping("public/auth/validateToken")
    fun validateToken(): Result<Boolean?> {
        return try {
            Result.success(AuthUtils.getCurrentUser() != null)
        } catch (e: Exception) {
            e.message
            Result.failure(e.message, false)
        }
    }

    /**
     * 获取在线用户
     * TODO: 获取IP
     */
    @RequiresPermissions("auth:user:listOnlineUser")
    @PostMapping("auth/listOnlineUser")
    fun listOnlineUser(): Result<List<OnlineUserVO>?> {
        return try {
            Result.success(this.authService.listOnlineUser())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 移除用户
     */
    @RequiresPermissions("auth:user:remove")
    @Log("移除在线用户")
    @PostMapping("auth/removeUser")
    fun removeUser(@RequestBody userIdList: List<String>): Result<Int?> {
        return try {
            Result.success(this.authService.removeUser(userIdList))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 移除用户
     */
    @RequiresPermissions("auth:user:remove")
    @Log("移除session")
    @PostMapping("auth/removeSession")
    fun removeSession(@RequestBody sessionIdList: List<String>): Result<Int?> {
        return try {
            Result.success(this.authService.removeSession(sessionIdList))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 注册秘钥
     */
    @PostMapping("auth/registerKey")
    fun registerKey(@RequestBody key: String): Result<String?> {
        return try {
            val session = AuthUtils.getSession()
            if (session == null) {
                Result.failure("注册秘钥失败，获取用户session失败")
            } else {
                session.setAttribute(AuthConstants.CLIENT_PUBLIC_KEY, key)
                Result.success(RsaKeyUtils.getServerKey().pubKey)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 更新密码
     */
    @PostMapping("auth/updatePassword")
    fun updatePassword(@RequestBody updatePasswordPO: UpdatePasswordPO): Result<Boolean?> {
        try {
            // 查询用户
            val user = this.userService.getById(AuthUtils.getCurrentUserId()) ?: return Result.success(false, "用户为登录")
            if (user.password != updatePasswordPO.oldPassword) {
                return Result.success(false, "原密码错误")
            }
            // 更新密码
            val updateQuery = KtUpdateWrapper(SysUserDO :: class.java)
                    .set(SysUserDO :: password, updatePasswordPO.password)
                    .eq(SysUserDO :: userId, user.userId)
            this.userService.update(updateQuery)
            // 退出登录
            SecurityUtils.getSubject().logout()
            return Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message)
        }
    }
}