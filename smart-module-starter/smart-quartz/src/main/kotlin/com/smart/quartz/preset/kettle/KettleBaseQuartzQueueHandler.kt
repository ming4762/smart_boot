package com.smart.quartz.preset.kettle

import com.smart.common.spring.ApplicationContextRegister
import com.smart.quartz.model.SmartTimedTaskDO
import com.smart.quartzqueue.common.handler.QuartzQueueHandler
import com.smart.starter.kettle.service.KettleService
import com.smart.starter.quartz.constants.QuartzConstants
import org.slf4j.LoggerFactory

/**
 * kettle基础执行类
 * @author ming
 * 2019/7/17 上午9:27
 */
abstract class KettleBaseQuartzQueueHandler : QuartzQueueHandler() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(KettleBaseQuartzQueueHandler::class.java)
    }

    override fun handle() {
        try {
            // 获取kettle服务
            val kettleService = ApplicationContextRegister.getBean(KettleService::class.java)
            if (kettleService != null) {
                // 获取kettle元数据
                val task = this.jobContext?.jobDetail?.jobDataMap?.get(QuartzConstants.JOB_MATE_DATA.name) as SmartTimedTaskDO?
                val parameter = task?.taskParameter
                if (parameter != null) {
                    this.executeKettle(kettleService, parameter)
                } else {
                    LOGGER.warn("未找到kettle名称，执行kettle失败")
                }
            } else {
                LOGGER.warn("未找到kettle服务")
            }
        } catch (e: Exception) {
            LOGGER.error("执行kettle发生错误", e)
        }
    }


    /**
     * 获取队列名称
     */
    override fun getQueueName(): String {
        val task = this.jobContext?.jobDetail?.jobDataMap?.get(QuartzConstants.JOB_MATE_DATA.name) as SmartTimedTaskDO?
        return task?.queueName ?: super.getQueueName()
    }

    abstract fun executeKettle(kettleService: KettleService, parameter: String)
}