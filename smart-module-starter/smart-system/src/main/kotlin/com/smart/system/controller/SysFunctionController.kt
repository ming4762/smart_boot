package com.smart.system.controller

import com.smart.common.message.Result
import com.smart.common.model.Tree
import com.smart.common.utils.TreeUtils
import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysFunctionDO
import com.smart.system.service.SysFunctionService
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
    @RequestMapping("/listAllTree")
    fun listAllTree(@RequestBody parameters: Map<String, Any?>): Result<Any?> {
        val result = this.list(parameters)
        val list = result.data
        if (list != null && list is List<*> && list.isNotEmpty()) {
            list as List<SysFunctionDO>
            return Result.success(TreeUtils.build(
                    list.map {
                        val tree = Tree<SysFunctionDO>()
                        tree.id = it.functionId
                        tree.text = it.functionName
                        tree.`object` = it
                        return@map tree
                    }
            ))
        }
        return result
    }
}