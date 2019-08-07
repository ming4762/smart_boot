package com.smart.portal.controller

import com.smart.portal.model.PortalPicDO
import com.smart.portal.service.PortalPicService
import com.smart.starter.crud.controller.BaseController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/8/1 下午3:46
 */
@RestController
@RequestMapping("portal/material/pic")
class PortalPicController : BaseController<PortalPicService, PortalPicDO>() {
}