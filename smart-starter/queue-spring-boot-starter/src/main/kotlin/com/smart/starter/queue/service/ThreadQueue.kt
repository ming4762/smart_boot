package com.smart.starter.queue.service

import com.smart.starter.queue.handler.QueueTaskHandler
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

/**
 * 线程队列实体类
 * @author ming
 * 2019/7/4 上午10:43
 */
class ThreadQueue(val name: String) {

    // 线程
    var threadExecutor = Executors.newSingleThreadExecutor()
    // 队列
    val queue = LinkedBlockingQueue<QueueTaskHandler>()

    // 运行状态
    var running = true


    /**
     * 添加任务到队列
     */
    fun addTask(task: QueueTaskHandler) {
        this.queue.put(task)
    }

    /**
     * 添加任务到队列
     */
    fun addTask(vararg tasks: QueueTaskHandler) {
        tasks.forEach {
            this.queue.put(it)
        }
    }

    /**
     * 添加任务到队列
     */
    fun addTask(tasks: List<QueueTaskHandler>) {
        tasks.forEach {
            this.queue.put(it)
        }
    }
}