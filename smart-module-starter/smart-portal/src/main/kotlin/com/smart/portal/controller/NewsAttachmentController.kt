package com.smart.portal.controller

import com.smart.portal.model.NewsAttachmentDO
import com.smart.portal.service.NewsAttachmentService
import com.smart.starter.crud.controller.BaseController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 附件管理
 * @author ming
 * 2019/8/23 上午9:00
 */
@RestController
@RequestMapping("portal/newsAttachment")
class NewsAttachmentController : BaseController<NewsAttachmentService, NewsAttachmentDO>() {
}