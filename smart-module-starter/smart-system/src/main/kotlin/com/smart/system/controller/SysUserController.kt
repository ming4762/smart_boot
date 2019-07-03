package com.smart.system.controller

import com.smart.auth.common.model.SysUserDO
import com.smart.common.message.Result
import com.smart.starter.crud.controller.BaseController
import com.smart.system.service.SysUserService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/6/12 下午2:32
 */
@RestController
@RequestMapping("/sys/user")
class SysUserController : BaseController<SysUserService, SysUserDO>() {

    override fun list(@RequestBody parameters: Map<String, Any?>): Result<Any?> {
        return super.list(parameters)
    }

    /**
     * 查询用户权限
     */
    @RequestMapping("/queryPermissionList")
    fun queryPermissionList(@RequestBody userIdList: List<String>): Result<Map<String, Set<String>>?> {
        return try {
            Result.success(this.service.queryPermissionList(userIdList))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}