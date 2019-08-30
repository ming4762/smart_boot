package com.smart.system.controller

import com.smart.common.message.Result
import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysDictItemDO
import com.smart.system.service.SysDictItemService
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("sys/dictItem")
class SysDictItemController : BaseController<SysDictItemService, SysDictItemDO>() {

    companion object {
        private const val SAVE = "sys:dictItem:save"
        private const val DELETE = "sys:dictItem:delete"
        private const val UPDATE = "sys:dictItem:update"
    }

    @RequiresPermissions(SAVE, UPDATE, logical = Logical.OR)
    override fun saveUpdate(@RequestBody t: SysDictItemDO): Result<Any?> {
        return super.saveUpdate(t)
    }
    @RequiresPermissions(SAVE)
    override fun save(@RequestBody t: SysDictItemDO): Result<Boolean?> {
        return super.save(t)
    }

    @RequiresPermissions(DELETE)
    override fun batchDelete(@RequestBody deleteObjects: MutableList<SysDictItemDO>?): Result<Any> {
        return super.batchDelete(deleteObjects)
    }

    @RequiresPermissions(UPDATE)
    override fun update(@RequestBody t: SysDictItemDO): Result<Boolean?> {
        return super.update(t)
    }
}