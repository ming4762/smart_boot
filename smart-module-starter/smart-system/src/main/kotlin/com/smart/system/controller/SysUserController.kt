package com.smart.system.controller

import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysUserDO
import com.smart.system.service.SysUserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 *
 * @author ming
 * 2019/6/12 下午2:32
 */
@Controller
@RequestMapping("/sys/user")
class SysUserController : BaseController<SysUserService, SysUserDO>() {
}