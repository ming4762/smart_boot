package com.smart.ui.system.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

/**
 *
 * @author ming
 * 2019/6/12 下午4:45
 */
@Controller
@RequestMapping("/ui/system")
class SysUiController {

    /**
     * 跳转到用户增删改查页面
     */
    @RequestMapping("/userCrud")
    fun userCrud(@RequestParam parameter: Map<String, Any>): ModelAndView {
        return ModelAndView("system/user/userCrud", parameter)
    }
}