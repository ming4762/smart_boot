package com.smart.system.controller

import com.smart.common.message.Result
import com.smart.starter.crud.constants.CRUDConstants
import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysRoleDO
import com.smart.system.service.SysRoleService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/6/12 下午3:50
 */
@RestController
@RequestMapping("/sys/role")
class SysRoleController : BaseController<SysRoleService, SysRoleDO>() {

    /**
     * 查询所有
     * todo：权限
     */
    @RequestMapping("/listWithAll")
    fun listWithAll(@RequestBody parameter: MutableMap<String, Any?>): Result<Any?> {
        parameter[CRUDConstants.WITH_ALL.name] = true
        return this.list(parameter)
    }
}