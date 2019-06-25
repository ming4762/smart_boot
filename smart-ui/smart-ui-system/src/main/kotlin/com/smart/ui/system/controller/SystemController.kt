package com.smart.ui.system.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

/**
 * 系统模块跳转
 * @author ming
 * 2019/6/24 上午11:07
 */
@RequestMapping("/ui/system")
@Controller
class SystemController {

    // 后台地址
    @Value("\${smart.ui.apiURL:}")
    private lateinit var apiURL: String

    /**
     * 跳转到登录页
     */
    @RequestMapping("/login")
    fun login(@RequestParam parameter: MutableMap<String, Any?>): ModelAndView {
        parameter["apiURL"] = this.apiURL
        return ModelAndView("system/login/login", parameter)
    }

    /**
     * 跳转到主页
     */
    @RequestMapping("/home")
    fun home(@RequestParam parameter: Map<String, Any?>): ModelAndView {
        return ModelAndView("system/home/home", parameter)
    }
}