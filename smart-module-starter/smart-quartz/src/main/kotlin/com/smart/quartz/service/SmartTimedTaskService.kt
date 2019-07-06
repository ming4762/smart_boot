package com.smart.quartz.service

import com.smart.quartz.model.SmartTimedTaskDO
import com.smart.starter.crud.service.BaseService

/**
 *
 * @author ming
 * 2019/7/4 下午4:56
 */
interface SmartTimedTaskService : BaseService<SmartTimedTaskDO> {

    fun openClose(taskIdList: List<String>, enable: Boolean)

    fun openClose(taskList: List<SmartTimedTaskDO>)
}