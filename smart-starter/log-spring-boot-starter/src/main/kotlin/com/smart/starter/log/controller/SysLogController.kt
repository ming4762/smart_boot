package com.smart.starter.log.controller

import com.smart.common.message.Result
import com.smart.starter.crud.constants.CRUDConstants
import com.smart.starter.crud.controller.BaseController
import com.smart.starter.crud.query.PageQueryParameter
import com.smart.starter.log.model.SysLogDO
import com.smart.starter.log.service.SysLogService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/6/28 下午4:22
 */
@RestController
@RequestMapping("/sys/log")
class SysLogController : BaseController<SysLogService, SysLogDO>() {

    @RequestMapping("/listWithAll")
    fun listWithAll(@RequestBody parameter: PageQueryParameter): Result<Any?> {
        parameter[CRUDConstants.WITH_ALL.name] = true
        return this.list(parameter)
    }
}