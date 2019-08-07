package com.smart.ui.portal.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

/**
 *
 * @author ming
 * 2019/8/1 下午3:56
 */
@Controller
@RequestMapping("ui/portal")
class PortalUiController {


    /**
     * 跳转到素材管理页面
     */
    @RequestMapping("/material")
    fun material(parameter: Map<String, String>): ModelAndView {
        return ModelAndView("portal/material/material", parameter)
    }
}