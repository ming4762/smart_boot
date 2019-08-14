package com.smart.ui.common.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

/**
 *
 * @author ming
 * 2019/8/14 上午10:21
 */
@Controller
@RequestMapping("ui/common")
class WebController {

    @RequestMapping
    fun common(@RequestParam parameter: Map<String, String>): ModelAndView {
        return ModelAndView("common/commonPage", parameter)
    }
}