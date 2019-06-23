package com.smart.auth.controller

import com.smart.auth.AuthProperties
import com.smart.auth.constants.AuthConstants
import com.smart.auth.model.po.LoginPO
import com.smart.common.message.Result
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

    /**
     * 登录接口
     */
    @PostMapping("/public/login")
    fun login(@RequestBody user: LoginPO): Result<Map<String, Any>?> {
        return this.doLogin(user.username, user.password, AuthConstants.LOGIN_WEB_TYPE)
    }

    /**
     * 移动端登录接口
     */
    @PostMapping("/public/mobileLogin")
    fun mobileLogin(@RequestBody user: LoginPO): Result<Map<String, Any>?> {
        return this.doLogin(user.username, user.password, AuthConstants.LOGIN_MOBILE_TYPE)
    }

    /**
     * 执行登录
     * @param 登录类型（web、移动）
     */
    private fun doLogin(username: String?, password: String?, type: AuthConstants): Result<Map<String, Any>?> {
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
            // 返回数据
            Result.success(mapOf(
                    // token信息
                    HttpHeaders.AUTHORIZATION to session.id.toString()
                    // todo:其他信息待完善
            ))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}