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
    // 是否启用接口加密
    @Value("\${smart.auth.useIde:false}")
    private var useIde: Boolean = false
    @Value("\${smart.ui.indexPage:}")
    private lateinit var indexPage: String

    /**
     * 跳转到登录页
     */
    @RequestMapping("/login")
    fun login(@RequestParam parameter: MutableMap<String, Any?>): ModelAndView {
        parameter["apiURL"] = this.apiURL
        parameter["useIde"] = this.useIde
        return ModelAndView("system/login/login", parameter)
    }

    /**
     * 跳转到主页
     */
    @RequestMapping("/home")
    fun home(@RequestParam parameter: MutableMap<String, Any?>): ModelAndView {
        parameter["indexPage"] =this.indexPage
        return ModelAndView("system/home/home", parameter)
    }

    /**
     * 跳转到组织机构页面
     */
    @RequestMapping("/organ")
    fun organ(@RequestParam parameter: Map<String, Any?>): ModelAndView {
        return ModelAndView("system/organ/organ", parameter)
    }

    /**
     * 跳转到角色管理页面
     */
    @RequestMapping("/role")
    fun role(@RequestParam parameter: Map<String, Any?>): ModelAndView {
        return ModelAndView("system/role/role", parameter)
    }


    /**
     * 跳转菜单分类管理页面
     */
    @RequestMapping("/menuConfig")
    fun menuConfig(@RequestParam parameter: Map<String, Any?>): ModelAndView {
        return ModelAndView("system/menu/menuConfig", parameter)
    }

    /**
     * 跳转到403页面
     */
    @RequestMapping("/error404")
    fun error404(): ModelAndView {
        return ModelAndView("system/error/403")
    }

    /**
     * 跳转账户信息页面
     */
    @RequestMapping("/accountMessage")
    fun accountMessage(@RequestParam parameter: Map<String, Any?>): ModelAndView {
        return ModelAndView("system/auth/accountMessage", parameter)
    }



}