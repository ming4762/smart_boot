package com.smart.auth.shiro

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
import java.lang.reflect.Method

/**
 * 自定义代理
 * @author ming
 * 2019/7/17 下午3:03
 */
class CustomAuthorizationAttributeSourceAdvisor(var development: Boolean = false) : AuthorizationAttributeSourceAdvisor() {

    /**
     * 重写匹配类
     * 开发模式下不匹配（不进行权限拦截)
     * @author zhongming
     * TODO:初步测试，影响范围不确定，待进一步测试
     */
    override fun matches(method: Method, targetClass: Class<*>): Boolean {
        if (development) return false
        return super.matches(method, targetClass)
    }
}