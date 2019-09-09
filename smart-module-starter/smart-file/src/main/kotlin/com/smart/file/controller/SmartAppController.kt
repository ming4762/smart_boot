package com.smart.file.controller

import com.smart.file.model.SmartAppDO
import com.smart.file.service.SmartAppService
import com.smart.starter.crud.controller.BaseController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/9/7 下午4:36
 */
@RestController
@RequestMapping("app")
class SmartAppController : BaseController<SmartAppService, SmartAppDO>() {
}