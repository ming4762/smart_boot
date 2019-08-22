package com.smart.auth.common.utils

import com.smart.auth.common.model.SysUserDO
import org.apache.shiro.SecurityUtils
import org.apache.shiro.UnavailableSecurityManagerException
import org.apache.shiro.session.Session
import org.apache.shiro.subject.Subject

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
        var subject: Subject? = null
        try {
            subject = SecurityUtils.getSubject()
            return subject?.principal as SysUserDO?
        } catch (e: UnavailableSecurityManagerException) {
            return null
        }
    }

    fun getCurrentUserId(): String? {
        return this.getCurrentUser()?.userId
    }
}