package com.smart.quartz.controller

import com.smart.common.log.annotation.Log
import com.smart.common.message.Result
import com.smart.quartz.model.SmartTimedTaskDO
import com.smart.quartz.service.SmartTimedTaskService
import com.smart.starter.crud.controller.BaseController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/7/4 下午4:59
 */
@RestController
@RequestMapping("/quartz/timeTask")
class SmartTimedTaskController : BaseController<SmartTimedTaskService, SmartTimedTaskDO>() {

    @RequestMapping("/list")
    override fun list(@RequestBody parameters: Map<String, Any?>): Result<Any?> {
        val list = super.list(parameters)
        return list
    }


    /**
     * 开始关闭定时任务
     */
    @RequestMapping("/openClose")
    @Log("开启关闭定时任务")
    fun openClose(@RequestBody taskList: List<SmartTimedTaskDO>): Result<Boolean?> {
        return try {
            this.service.openClose(taskList)
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}