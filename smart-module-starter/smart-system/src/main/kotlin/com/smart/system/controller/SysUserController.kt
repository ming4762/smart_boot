package com.smart.system.controller

import com.smart.common.message.Result
import com.smart.starter.crud.controller.BaseController
import com.smart.system.service.SysUserService
import org.apache.shiro.authz.annotation.RequiresPermissions
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

    @RequiresPermissions("123")
    override fun list(parameters: Map<String, Any?>): Result<Any?> {
        return super.list(parameters)
    }
}