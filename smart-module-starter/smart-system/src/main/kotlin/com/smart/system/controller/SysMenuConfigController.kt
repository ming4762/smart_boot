package com.smart.system.controller

import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysMenuConfigDO
import com.smart.system.service.SysMenuConfigService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/6/29 下午7:11
 */
@RestController
@RequestMapping("/sys/menuConfig")
class SysMenuConfigController : BaseController<SysMenuConfigService, SysMenuConfigDO>() {
}