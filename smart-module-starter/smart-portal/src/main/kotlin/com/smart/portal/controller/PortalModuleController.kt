package com.smart.portal.controller

import com.smart.portal.model.PortalModuleDO
import com.smart.portal.service.PortalModuleService
import com.smart.starter.crud.controller.BaseController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/8/7 上午10:47
 */
@RestController
@RequestMapping("portal/module")
class PortalModuleController : BaseController<PortalModuleService, PortalModuleDO>() {

}