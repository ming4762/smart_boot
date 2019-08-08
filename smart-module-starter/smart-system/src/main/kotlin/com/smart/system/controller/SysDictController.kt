package com.smart.system.controller

import com.smart.common.message.Result
import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysDictDO
import com.smart.system.service.SysDictService
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("sys/dict")
class SysDictController : BaseController<SysDictService, SysDictDO>() {

    companion object {
        private const val SAVE = "sys:dict:save"
        private const val DELETE = "sys:dict:delete"
        private const val UPDATE = "sys:dict:update"
        private const val QUERY = "sys:dict:query"
    }

    @RequiresPermissions(QUERY)
    override fun list(parameters: MutableMap<String, Any?>): Result<Any?> {
        return super.list(parameters)
    }

    @RequiresPermissions(SAVE, UPDATE, logical = Logical.OR)
    override fun saveUpdate(t: SysDictDO): Result<Any?> {
        return super.saveUpdate(t)
    }
    @RequiresPermissions(SAVE)
    override fun save(t: SysDictDO): Result<Boolean?> {
        return super.save(t)
    }

    @RequiresPermissions(DELETE)
    override fun batchDelete(deleteObjects: MutableList<SysDictDO>?): Result<Any> {
        return super.batchDelete(deleteObjects)
    }

    @RequiresPermissions(UPDATE)
    override fun update(t: SysDictDO): Result<Boolean?> {
        return super.update(t)
    }
}