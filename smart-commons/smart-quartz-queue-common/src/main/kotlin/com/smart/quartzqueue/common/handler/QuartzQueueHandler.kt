package com.smart.quartzqueue.common.handler

import com.smart.common.queue.handler.QueueTaskHandler
import com.smart.starter.queue.service.QueueExecuteService
import com.smart.starter.queue.utils.QueueUtils
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean

/**
 *
 * @author ming
 * 2019/7/6 下午9:32
 */
abstract class QuartzQueueHandler : QuartzJobBean(), QueueTaskHandler {

    /**
     * 定时任务
     */
    final override fun executeInternal(context: JobExecutionContext) {
        QueueUtils.addTask(this, this.getQueueName())
    }

    /**
     * 执行队列
     */
    final override fun execute() {
        this.handle()
    }

    /**
     * 执行方法
     */
    abstract fun handle()

    fun getQueueName(): String {
        return QueueExecuteService.DEFAULT_QUEUE_NAME
    }
}