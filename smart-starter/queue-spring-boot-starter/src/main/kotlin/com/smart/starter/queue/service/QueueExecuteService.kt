package com.smart.starter.queue.service

import com.smart.common.queue.handler.QueueTaskHandler
import com.smart.starter.queue.QueueProperties
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * 队列执行服务
 * @author ming
 * 2019/7/4 上午10:12
 */
class QueueExecuteService(val queueProperties: QueueProperties) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(QueueExecuteService :: class.java)
        const val DEFAULT_QUEUE_NAME = "default"
    }

    // 队列容器
    private val queueContainer= mutableMapOf(
            DEFAULT_QUEUE_NAME to ThreadQueue(DEFAULT_QUEUE_NAME)
    )

    /**
     * 初始化函数
     */
    @PostConstruct
    private fun init () {
        this.queueContainer.forEach { _, threadQueue ->
            this.executeThreadQueue(threadQueue)
        }
    }


    /**
     * 执行队列
     */
    private fun executeThreadQueue(threadQueue: ThreadQueue) {
        threadQueue.threadExecutor.execute {
            while (threadQueue.running) {
                try {
                    // 获取任务
                    val task = threadQueue.queue.take()
                    try {
                        task.execute()
                    } catch (e: Exception) {
                        LOGGER.error("执行任务发生错误", e)
                    }
                } catch (e: InterruptedException) {
                    LOGGER.error("服务停止，退出", e)
                    threadQueue.running = false
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 添加任务
     */
    fun addTask(task: QueueTaskHandler, queueName: String = DEFAULT_QUEUE_NAME): Boolean {
        // 判断线程队列是否存在 不存在则创建
        val threadQueue = this.getThreadQueue(queueName)
        if (!threadQueue.running) {
            LOGGER.warn("队列线程已停止")
            return false
        }
        threadQueue.addTask(task)
        return true
    }

    /**
     * 添加任务
     */
    fun addTask(vararg tasks: QueueTaskHandler, queueName: String = DEFAULT_QUEUE_NAME): Boolean {
        // 获取线程队列
        val threadQueue = this.getThreadQueue(queueName)
        if (!threadQueue.running) {
            LOGGER.warn("队列线程已停止")
            return false
        }
        threadQueue.addTask(tasks.asList())
        return true
    }

    /**
     * 添加任务
     */
    fun addTask(tasks: List<QueueTaskHandler>, queueName: String = DEFAULT_QUEUE_NAME): Boolean {
        // 获取线程队列
        val threadQueue = this.getThreadQueue(queueName)
        if (!threadQueue.running) {
            LOGGER.warn("队列线程已停止")
            return false
        }
        threadQueue.addTask(tasks)
        return true
    }

    /**
     * 判断队列是否有任务
     */
    fun hasTask(queueName: String = DEFAULT_QUEUE_NAME): Boolean {
        // 判断线程队列是否存在
        val threadQueue = this.queueContainer[queueName]
        if (threadQueue == null) {
            LOGGER.warn("队列不存在")
            return false
        }
        return threadQueue.queue.isEmpty()
    }

    /**
     * 激活队列
     */
    fun activeQueue(queueName: String = DEFAULT_QUEUE_NAME): Boolean {
        val threadQueue = this.queueContainer[queueName]
        if (threadQueue == null) {
            LOGGER.warn("队列不存在")
            return false
        }
        threadQueue.running = true
        val threadExecutor = threadQueue.threadExecutor
        if (threadExecutor.isShutdown) {
            // 重新建立线程
            threadQueue.threadExecutor = Executors.newSingleThreadExecutor()
            this.executeThreadQueue(threadQueue)
            LOGGER.info("线程池关闭，重新初始化线程池及任务")
        }
        return true
    }

    /**
     * 移除队列
     */
    fun removeQueue(queueName: String = DEFAULT_QUEUE_NAME) {
        // 关闭队列线程
        this.queueContainer[queueName]?.threadExecutor?.shutdownNow()
        this.queueContainer[queueName]?.running = false
        this.queueContainer.remove(queueName)
    }

    /**
     * 获取线程队列
     */
    private fun getThreadQueue(queueName: String): ThreadQueue {
        // 判断线程队列是否存在 不存在则创建
        var threadQueue = this.queueContainer[queueName]
        if (threadQueue == null) {
            // 创建队列并放入容器中
            threadQueue = ThreadQueue(queueName)
            this.queueContainer[queueName] = threadQueue
            // 执行队列
            this.executeThreadQueue(threadQueue)
        }
        return threadQueue
    }

    /**
     * 对象销毁
     */
    @PreDestroy
    private fun destory() {
        this.queueContainer.values.forEach {
            it.running = false
            it.threadExecutor.shutdownNow()
        }
    }

}