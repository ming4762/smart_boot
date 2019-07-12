package com.smart.quartz.preset.kettle

import com.smart.common.spring.ApplicationContextRegister
import com.smart.quartz.model.SmartTimedTaskDO
import com.smart.quartzqueue.common.handler.QuartzQueueHandler
import com.smart.starter.kettle.service.KettleService
import com.smart.starter.quartz.constants.QuartzConstants
import org.slf4j.LoggerFactory

/**
 * kettle资源库执行预设
 * @author ming
 * 2019/7/12 上午8:47
 */
class RepositoryTransQuartzQueueHandler : QuartzQueueHandler() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RepositoryTransQuartzQueueHandler::class.java)
    }

    override fun handle() {
        try {
            // 获取kettle服务
            val kettleService = ApplicationContextRegister.getBean(KettleService::class.java)
            if (kettleService != null) {
                // 获取kettle元数据
                val task = this.jobContext?.jobDetail?.jobDataMap?.get(QuartzConstants.JOB_MATE_DATA.name) as SmartTimedTaskDO?
                if (task?.taskParameter != null) {
                    val transName = task.taskParameter!!
                    kettleService.excuteDBTransfer(transName)
                } else {
                    LOGGER.warn("未找到转换名称")
                }
            } else {
                LOGGER.warn("未找到kettle服务")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}