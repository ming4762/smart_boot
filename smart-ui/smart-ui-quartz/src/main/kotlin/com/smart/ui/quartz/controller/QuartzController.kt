package com.smart.ui.quartz.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

/**
 *
 * @author ming
 * 2019/7/5 上午10:48
 */
@Controller
@RequestMapping("/ui/quartz")
class QuartzController {

    /**
     * 跳转到定时任务管理页面
     */
    @RequestMapping("/timedTask")
    fun timedTask(@RequestParam parameter: Map<String, Any>): ModelAndView {
        return ModelAndView("quartz/timedTask/timedTask", parameter)
    }
}