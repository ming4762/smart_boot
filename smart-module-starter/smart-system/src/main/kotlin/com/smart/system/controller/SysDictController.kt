package com.smart.system.controller

import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysDictDO
import com.smart.system.service.SysDictService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("sys/dict")
class SysDictController : BaseController<SysDictService, SysDictDO>() {
}