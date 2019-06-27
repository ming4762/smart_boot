package com.smart.system.controller

import com.smart.common.message.Result
import com.smart.common.model.Tree
import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysOrganDO
import com.smart.system.service.SysOrganService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 组织机构controller
 * @author ming
 * 2019/6/26 下午4:01
 */
@RestController
@RequestMapping("sys/organ")
class SysOrganController : BaseController<SysOrganService, SysOrganDO>() {

    // TODO: 权限
    @RequestMapping("/listTree")
    fun listTree(@RequestBody topParentIdList: List<String>): Result<Map<String, List<Tree<SysOrganDO>>?>?> {
        return try {
            Result.success(this.service.listTree(topParentIdList))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

}