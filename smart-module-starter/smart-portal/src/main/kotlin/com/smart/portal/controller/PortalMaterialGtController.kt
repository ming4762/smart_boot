package com.smart.portal.controller

import com.smart.portal.model.PortalMaterialGtDO
import com.smart.portal.service.PortalMaterialGtService
import com.smart.starter.crud.controller.BaseController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 图文素材controller
 * @author ming
 * 2019/8/1 下午2:47
 */
@RestController
@RequestMapping("/portal/material/gt")
class PortalMaterialGtController : BaseController<PortalMaterialGtService, PortalMaterialGtDO>() {
}