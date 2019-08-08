package com.smart.system.controller

import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysDictItemDO
import com.smart.system.service.SysDictItemService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("sys/dictItem")
class SysDictItemController : BaseController<SysDictItemService, SysDictItemDO>() {
}