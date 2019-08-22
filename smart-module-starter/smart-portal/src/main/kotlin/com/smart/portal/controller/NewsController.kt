package com.smart.portal.controller

import com.smart.portal.model.NewsDO
import com.smart.portal.service.NewsService
import com.smart.starter.crud.controller.BaseController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/news")
class NewsController : BaseController<NewsService, NewsDO>() {
}