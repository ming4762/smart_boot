package com.smart.auth.common.utils

import com.smart.auth.common.model.SysUserDO
import org.apache.shiro.SecurityUtils
import org.apache.shiro.session.Session

/**
 * 认证工具类
 * @author ming
 * 2019/6/22 下午8:59
 */
object AuthUtils {

    /**
     * 获取session信息
     */
    fun getSession(): Session? {
        return SecurityUtils.getSubject().session
    }

    /**
     * 获取当前用户信息
     */
    fun getCurrentUser(): SysUserDO? {
        return SecurityUtils.getSubject().principal as SysUserDO?
    }

    fun getCurrentUserId(): String? {
        return this.getCurrentUser()?.userId
    }
}