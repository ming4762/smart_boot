package com.smart.ui.activiti.editor.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

/**
 *
 * @author ming
 * 2019/10/24 下午4:31
 */
@Controller
@RequestMapping("activiti/web")
class ActivitiWebController {


    @RequestMapping("edit")
    fun edit(parameter: Map<String, String>): ModelAndView {
        return ModelAndView("activiti/edit", parameter)
    }
}