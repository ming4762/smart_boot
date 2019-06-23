package com.smart.auth.shiro

import org.apache.http.HttpHeaders
import org.apache.shiro.web.servlet.ShiroHttpServletRequest
import org.apache.shiro.web.servlet.ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager
import org.apache.shiro.web.util.WebUtils
import org.springframework.util.StringUtils
import java.io.Serializable
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

/**
 * 自定义session管理器
 * 实现前后端分离
 * @author ming
 * 2019/6/21 下午1:54
 */
class TokenSessionManager : DefaultWebSessionManager() {

    /**
     * 重写获取sessionId策略
     */
    override fun getSessionId(request: ServletRequest, response: ServletResponse): Serializable? {
        // 获取token
        val token = WebUtils.toHttp(request).getHeader(HttpHeaders.AUTHORIZATION)
        if (!StringUtils.isEmpty(token)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, true);
            return token
        }
        return super.getSessionId(request, response)
    }
}