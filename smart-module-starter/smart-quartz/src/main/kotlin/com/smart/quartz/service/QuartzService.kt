package com.smart.quartz.service

import com.smart.quartz.model.SmartTimedTaskDO
import org.quartz.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

/**
 * 定时任务服务类
 * @author ming
 * 2019/7/4 下午8:45
 */
@Component
class QuartzService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(QuartzService :: class.java)
    }

    @Autowired
    private lateinit var schedulerFactoryBean: SchedulerFactoryBean

    /**
     * 添加定时任务
     */
    fun addTask(task: SmartTimedTaskDO) {
        try {
            val clazz = Class.forName(task.clazz)
            // 判断类是否是子类
            if (QuartzJobBean :: class.java.isAssignableFrom(clazz)) {
                clazz as Class<out Job>
                val scheduler = this.schedulerFactoryBean.scheduler
                val jobDetail = JobBuilder.newJob(clazz)
                        .withIdentity(task.taskId)
                        .build()
                jobDetail.jobDataMap["task"] = task
                val trigger = TriggerBuilder.newTrigger()
                        .withIdentity(task.taskId)
                        .withSchedule(CronScheduleBuilder.cronSchedule(task.cron))
                        .build()
                scheduler.scheduleJob(jobDetail, trigger)
            } else {
                LOGGER.warn("类【${task.clazz}】不是QuartzJobBean的子类")
            }
        } catch (e: ClassNotFoundException) {
            LOGGER.error("无法获取类：${task.clazz}")
        }
    }

    /**
     * 批量添加任务
     */
    fun addTask(vararg tasks: SmartTimedTaskDO) {
        tasks.forEach {
            this.addTask(it)
        }
    }

    /**
     * 批量添加任务
     */
    fun addTask(tasks: Collection<SmartTimedTaskDO>) {
        tasks.forEach {
            this.addTask(it)
        }
    }

    /**
     * 移除定时任务
     * @throws Exception
     */
    @Throws(Exception::class)
    fun removeTask(task: SmartTimedTaskDO): Boolean {
        val scheduler = this.schedulerFactoryBean.scheduler
        return scheduler.deleteJob(this.getJobKeyByTask(task))
    }

    /**
     * 移除定时任务
     */
    @Throws(Exception::class)
    fun removeTask(taskList: List<SmartTimedTaskDO>): Boolean {
        val scheduler = this.schedulerFactoryBean.scheduler
        return scheduler.deleteJobs(
                taskList.map { this.getJobKeyByTask(it) }
        )
    }

    /**
     * 重启定时任务
     * @param cloudTimedTask
     * @throws Exception
     */
    @Throws(Exception::class)
    fun resumeTask(task: SmartTimedTaskDO) {
        val scheduler = this.schedulerFactoryBean.scheduler
        scheduler.resumeJob(this.getJobKeyByTask(task))
    }

    /**
     * 暂停定时任务
     * @param cloudTimedTask
     * @throws Exception
     */
    @Throws(Exception::class)
    fun pauseTask(task: SmartTimedTaskDO) {
        val scheduler = this.schedulerFactoryBean.scheduler
        scheduler.pauseJob(this.getJobKeyByTask(task))
    }

    /**
     * 获取任务信息
     * @param cloudTimedTask
     * @return
     */
    @Throws(Exception::class)
    fun getJob(task: SmartTimedTaskDO): JobDetail? {
        val scheduler = this.schedulerFactoryBean.scheduler
        return scheduler.getJobDetail(this.getJobKeyByTask(task))
    }

    /**
     * 根据任务信息获取jobkey
     * @param cloudTimedTask
     * @return
     */
    private fun getJobKeyByTask(task: SmartTimedTaskDO): JobKey {
        return JobKey.jobKey(task.taskId)
    }
}