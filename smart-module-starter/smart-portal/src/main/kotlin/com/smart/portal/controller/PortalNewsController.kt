package com.smart.portal.controller

import com.smart.common.message.Result
import com.smart.portal.model.NewsDO
import com.smart.portal.service.NewsService
import com.smart.starter.crud.constants.CRUDConstants
import com.smart.starter.crud.controller.BaseController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("portal/news")
class PortalNewsController : BaseController<NewsService, NewsDO>() {

    /**
     * 查询所有
     */
    @PostMapping("listWthAll")
    fun listWthAll(@RequestBody parameter: MutableMap<String, Any?>): Result<Any?> {
        parameter[CRUDConstants.WITH_ALL.name] = true
        return super.list(parameter)
    }
}