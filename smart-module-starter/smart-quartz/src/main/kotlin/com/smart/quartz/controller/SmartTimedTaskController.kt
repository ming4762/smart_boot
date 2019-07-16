package com.smart.quartz.controller

import com.smart.common.log.annotation.Log
import com.smart.common.message.Result
import com.smart.quartz.model.SmartTimedTaskDO
import com.smart.quartz.preset.PresetClass
import com.smart.quartz.service.SmartTimedTaskService
import com.smart.starter.crud.controller.BaseController
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 定时任务接口类
 * @author ming
 * 2019/7/4 下午4:59
 */
@RestController
@RequestMapping("/quartz/timeTask")
class SmartTimedTaskController : BaseController<SmartTimedTaskService, SmartTimedTaskDO>() {

    @RequiresPermissions("quartz:timeTask:query")
    @RequestMapping("/list")
    override fun list(@RequestBody parameters: Map<String, Any?>): Result<Any?> {
        val list = super.list(parameters)
        return list
    }


    /**
     * 开始关闭定时任务
     */
    @RequiresPermissions("quartz:timeTask:add", "quartz:timeTask:update", logical = Logical.OR)
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

    /**
     * 查询预设类
     */
    @RequestMapping("/queryPreset")
    fun queryPreset(): Result<Map<String, String>?> {
        return try {
            val map = mutableMapOf<String, String>()
            PresetClass.values().iterator().forEach {
                map[it.name] = it.value

            }
            Result.success(map)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}