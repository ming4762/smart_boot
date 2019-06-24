package com.smart.ui.system.controller

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

    /**
     * 跳转到登录页
     */
    @RequestMapping("/login")
    fun login(@RequestParam parameter: Map<String, Any?>): ModelAndView {
        return ModelAndView("system/login/login", parameter)
    }
}