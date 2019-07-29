package com.smart.starter.ide.filter

import com.smart.starter.ide.request.ResettableStreamHttpServletRequest
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

/**
 * 接口加密拦截器，用于保存请求信息
 * @author zhongming
 */
class ParameterDecrypedFilter: Filter {
    override fun destroy() {
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        val resettableStreamHttpServletRequest = ResettableStreamHttpServletRequest(request)
        // 获取数据
        chain.doFilter(resettableStreamHttpServletRequest, response)
    }

    override fun init(filterConfig: FilterConfig?) {
    }
}