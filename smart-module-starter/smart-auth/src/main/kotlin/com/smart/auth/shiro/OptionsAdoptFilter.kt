package com.smart.auth.shiro

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter
import org.apache.shiro.web.util.WebUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Options请求不拦截过滤器
 * @author ming
 * 2019/6/21 下午2:16
 */
class OptionsAdoptFilter : FormAuthenticationFilter() {


    /**
     * 不拦截option请求
     */
//    override fun onAccessDenied(request: ServletRequest, response: ServletResponse, mappedValue: Any?): Boolean {
//        response as HttpServletResponse
//        request as HttpServletRequest
//        if (request.method == RequestMethod.OPTIONS.name) {
//            response.status = HttpStatus.OK.value()
//            return true
//        }
//        return super.onAccessDenied(request, response, mappedValue)
//    }

    /**
     * 重写认证策略，未登录接口返回401（不重定向）
     */
    override fun onAccessDenied(request: ServletRequest, response: ServletResponse): Boolean {
        response as HttpServletResponse
        request as HttpServletRequest
        if (request.method == RequestMethod.OPTIONS.name) {
            response.status = HttpStatus.OK.value()
            return true
        }
        val subject = getSubject(request, response)
        if (subject.principal == null) {
            WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED)
            return false
        }
        return true
    }
}