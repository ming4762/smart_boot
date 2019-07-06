package com.smart.starter.queue.utils

import com.smart.common.queue.handler.QueueTaskHandler
import com.smart.common.spring.ApplicationContextRegister
import com.smart.starter.queue.service.QueueExecuteService
import com.smart.starter.queue.service.QueueExecuteService.Companion.DEFAULT_QUEUE_NAME

/**
 * 队列工具类
 * @author ming
 * 2019/7/6 下午9:15
 */
object QueueUtils {

    private var queueExecuteService: QueueExecuteService? = null

    /**
     * 获取队列服务
     */
    private fun getQueueService(): QueueExecuteService {
        return if (queueExecuteService != null) {
            queueExecuteService!!
        } else {
            queueExecuteService = ApplicationContextRegister.getBean(QueueExecuteService :: class.java)
            if (queueExecuteService == null) {
                throw Exception("未找到QueueExecuteService实例")
            }
            queueExecuteService!!
        }
    }

    fun addTask(task: QueueTaskHandler, queueName: String = QueueExecuteService.DEFAULT_QUEUE_NAME): Boolean {
        return getQueueService().addTask(task, queueName)
    }

    fun addTask(vararg tasks: QueueTaskHandler, queueName: String = QueueExecuteService.DEFAULT_QUEUE_NAME): Boolean {
        return getQueueService().addTask(tasks.asList(), queueName)
    }

    fun addTask(tasks: List<QueueTaskHandler>, queueName: String = QueueExecuteService.DEFAULT_QUEUE_NAME): Boolean {
        return getQueueService().addTask(tasks, queueName)
    }

    fun hasTask(queueName: String = QueueExecuteService.DEFAULT_QUEUE_NAME): Boolean {
        return getQueueService().hasTask(queueName)
    }

    /**
     * 激活队列
     */
    fun activeQueue(queueName: String = DEFAULT_QUEUE_NAME): Boolean {
        return getQueueService().activeQueue(queueName)
    }

    /**
     * 移除队列
     */
    fun removeQueue(queueName: String = DEFAULT_QUEUE_NAME) {
        getQueueService().removeQueue(queueName)
    }
}