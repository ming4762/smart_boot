package com.smart.system.controller

import com.smart.starter.crud.controller.BaseControllerQuery
import com.smart.system.model.SysDictItemDO
import com.smart.system.service.SysDictItemService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 查询字典项
 * @author ming
 * 2019/8/30 下午1:34
 */
@RestController
@RequestMapping("public/sys/dictItem")
class SysDictItemPublicController : BaseControllerQuery<SysDictItemService, SysDictItemDO>() {
}