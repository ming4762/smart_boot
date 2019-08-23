package com.smart.ui.portal.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
    fun material(@RequestParam parameter: Map<String, String>): ModelAndView {
        return ModelAndView("portal/material/material", parameter)
    }

    /**
     * 跳转到模块管理页面
     */
    @RequestMapping("/module")
    fun module(@RequestParam parameter: Map<String, String>): ModelAndView {
        return ModelAndView("portal/module/module", parameter)
    }

    /**
     * 查看新闻详情
     */
    @RequestMapping("newsDetail/{id}")
    fun newsDetail(@PathVariable("id") id: String): ModelAndView {
        val parameter = mapOf(
                "newsId" to id
        )
        return ModelAndView("portal/news/newsDetail", parameter)
    }
}