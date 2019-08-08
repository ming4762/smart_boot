package com.smart.auth.controller

import com.smart.auth.AuthProperties
import com.smart.auth.common.constants.AuthConstants
import com.smart.auth.common.utils.AuthUtils
import com.smart.auth.model.po.LoginPO
import com.smart.common.message.Result
import com.smart.common.utils.IPUtils
import com.smart.system.service.SysUserService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * 登录接口
 * @author ming
 * 2019/6/21 下午3:00
 */
@RestController
@RequestMapping
class LoginController {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LoginController :: class.java)
    }

    @Autowired
    private lateinit var authProperties: AuthProperties

    @Autowired
    private lateinit var userService: SysUserService


    /**
     * 登录接口
     */
    @PostMapping("/public/login")
    fun login(@RequestBody user: LoginPO, request: HttpServletRequest): Result<Map<String, Any>?> {
        return this.doLogin(request, user.username, user.password, AuthConstants.LOGIN_WEB_TYPE)
    }

    /**
     * 移动端登录接口
     */
    @PostMapping("/public/mobileLogin")
    fun mobileLogin(@RequestBody user: LoginPO, request: HttpServletRequest): Result<Map<String, Any>?> {
        return this.doLogin(request, user.username, user.password, AuthConstants.LOGIN_MOBILE_TYPE)
    }

    /**
     * 执行登录
     * @param 登录类型（web、移动）
     */
    private fun doLogin(request: HttpServletRequest, username: String?, password: String?, type: AuthConstants): Result<Map<String, Any>?> {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            LOGGER.warn("用户名或密码为空，username：{}  password：{}", username, password)
            return Result.failure(403, "用户名或密码不能为空")
        }
        return try {
            // 执行登录
            SecurityUtils.getSubject().login(UsernamePasswordToken(username, password))
            val session = SecurityUtils.getSubject().session
            // 设置session的超时事件
            if (type == AuthConstants.LOGIN_WEB_TYPE) {
                session.timeout = this.authProperties.session.timeout.global * 1000
            } else if (type == AuthConstants.LOGIN_MOBILE_TYPE) {
                session.timeout = this.authProperties.session.timeout.mobile * 1000
            }
            val permissionList = this.queryUserPremissionList()
            session.setAttribute(AuthConstants.PERMISSION, permissionList)
            session.setAttribute(AuthConstants.LOGIN_IP, IPUtils.getIpAddr(request))
            // 返回数据
            val user = AuthUtils.getCurrentUser()
            user?.let {
                it.password = null
            }
            val data = mutableMapOf(
                    HttpHeaders.AUTHORIZATION to session.id.toString(),
                    "permission" to permissionList
            )
            user?.let {
                it.password = null
                data["user"] = it
            }
            Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 查询用户权限信息
     */
    private fun queryUserPremissionList(): Set<String> {
        val userId = AuthUtils.getCurrentUserId() ?: return setOf()
        val premissionMap: Map<String, Set<String>> = this.userService.queryPermissionList(listOf(userId))
        return premissionMap[userId] ?: setOf()
    }
}