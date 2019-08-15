package com.smart.ui.common.interceptor

import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 设置环境信息
 * @author ming
 * 2019/8/15 上午8:23
 */
class EnvInterceptor(val profiles: String, val development: String) : HandlerInterceptor {

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        if (modelAndView != null) {
            var development = false
            if (this.development != "") {
                development = java.lang.Boolean.valueOf(this.development)
            } else if (this.profiles != "pro") {
                development = true
            }
            modelAndView.model["development"] = development
        }
        super.postHandle(request, response, handler, modelAndView)
    }
}