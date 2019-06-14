package com.smart.system.controller

import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysRoleDO
import com.smart.system.service.SysRoleService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 *
 * @author ming
 * 2019/6/12 下午3:50
 */
@Controller
@RequestMapping("/sys/role")
class SysRoleController : BaseController<SysRoleService, SysRoleDO>() {
}