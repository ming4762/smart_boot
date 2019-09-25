package com.smart.portal.controller

import com.smart.common.message.Result
import com.smart.portal.model.NewsDO
import com.smart.portal.service.NewsService
import com.smart.starter.crud.constants.CRUDConstants
import com.smart.starter.crud.controller.BaseControllerQuery
import com.smart.starter.crud.query.PageQueryParameter
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/8/23 下午4:37
 */
@RestController
@RequestMapping("public/portal/news")
class PortalNewsPublicController : BaseControllerQuery<NewsService, NewsDO>() {
    /**
     * 查询所有
     */
    @PostMapping("listWthAll")
    fun listWthAll(@RequestBody parameter: PageQueryParameter): Result<Any?> {
        parameter[CRUDConstants.WITH_ALL.name] = true
        return super.list(parameter)
    }

    @PostMapping("queryDetail")
    fun queryDetail(@RequestBody newsId: String): Result<NewsDO?> {
        return try {
            val news = NewsDO()
            news.newsId = newsId
            Result.success(this.service.queryDetail(news))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}