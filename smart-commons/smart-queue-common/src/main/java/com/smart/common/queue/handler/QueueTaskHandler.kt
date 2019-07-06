package com.smart.common.queue.handler

/**
 * 队列执行器
 * @author ming
 * 2019/7/4 上午10:10
 */
interface QueueTaskHandler {

    /**
     * 执行队列
     */
    fun execute()
}