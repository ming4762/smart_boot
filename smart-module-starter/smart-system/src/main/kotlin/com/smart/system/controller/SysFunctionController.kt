package com.smart.system.controller

import com.smart.common.log.annotation.Log
import com.smart.common.message.Result
import com.smart.common.model.Tree
import com.smart.starter.crud.controller.BaseController
import com.smart.starter.crud.query.PageQueryParameter
import com.smart.system.model.SysFunctionDO
import com.smart.system.service.SysFunctionService
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 功能管理
 * @author ming
 * 2019/6/23 下午1:43
 */
@RestController
@RequestMapping("/sys/function")
class SysFunctionController : BaseController<SysFunctionService, SysFunctionDO>() {

    /**
     * 查询功能树形结构
     */
    @RequiresPermissions("system:function:query")
    @RequestMapping("/queryFunctionTree")
    fun queryFunctionTree(@RequestBody parameters: PageQueryParameter): Result<List<Tree<SysFunctionDO>>?> {
        return try {
            val menuId = parameters["menuId"]
            if (menuId == null) {
                Result.success(this.service.queryAllFunctionTree(parameters))
            } else {
                Result.success(this.service.queryWithChildren(parameters))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    @RequiresPermissions("system:function:query")
    override fun list(@RequestBody parameter: PageQueryParameter): Result<Any?> {
        return super.list(parameter)
    }

    @RequiresPermissions("system:function:save", "system:function:update", logical = Logical.OR)
    @Log("保存/修改功能")
    override fun saveUpdate(@RequestBody t: SysFunctionDO): Result<Any?> {
        return super.saveUpdate(t)
    }

    @RequiresPermissions("system:function:delete")
    @Log("删除功能")
    override fun batchDelete(@RequestBody deleteObjects: List<SysFunctionDO>): Result<Any?> {
        return super.batchDelete(deleteObjects)
    }
}