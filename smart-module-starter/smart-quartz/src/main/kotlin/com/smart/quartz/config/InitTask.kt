package com.smart.quartz.config

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.quartz.model.SmartTimedTaskDO
import com.smart.quartz.service.QuartzService
import com.smart.quartz.service.SmartTimedTaskService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * 项目启动初始化任务
 * @author ming
 * 2019/7/5 下午8:57
 */
@Component
class InitTask : ApplicationRunner {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(InitTask :: class.java)
    }

    @Autowired
    private lateinit var taskService: SmartTimedTaskService

    @Autowired
    private lateinit var quartzService: QuartzService

    override fun run(applicationArguments: ApplicationArguments) {
        LOGGER.debug("初始化定时任务")
        // 获取启动的任务
        val usedTaskList = taskService.list(
                KtQueryWrapper(SmartTimedTaskDO::class.java).eq(SmartTimedTaskDO :: used, true)
        )
        // 添加任务
        this.quartzService.addTask(usedTaskList)
    }
}