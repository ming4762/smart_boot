package com.smart.portal.controller

import com.smart.portal.model.PortalModuleDO
import com.smart.portal.service.PortalModuleService
import com.smart.starter.crud.controller.BaseControllerQuery
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 公共模块controller
 * @author ming
 * 2019/8/7 上午10:56
 */
@RestController
@RequestMapping("public/portal/module")
class PortalModulePublicController : BaseControllerQuery<PortalModuleService, PortalModuleDO>() {
}