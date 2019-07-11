package com.smart.starter.quartz.service

import com.smart.starter.quartz.properties.QuartzMetaData
import org.quartz.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

/**
 * 定时任务服务类
 * @author ming
 * 2019/7/11 下午4:57
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
    fun <T> addTask(metaData: QuartzMetaData<T>) {
        try {
            val clazz = Class.forName(metaData.clazz)
            if (QuartzJobBean :: class.java.isAssignableFrom(clazz)) {
                clazz as Class<out Job>
                val scheduler = this.schedulerFactoryBean.scheduler
                val jobDetail = JobBuilder.newJob(clazz)
                        .withIdentity(metaData.id)
                        .build()
                jobDetail.jobDataMap["task"] = metaData.data
                val trigger = TriggerBuilder.newTrigger()
                        .withIdentity(metaData.id)
                        .withSchedule(CronScheduleBuilder.cronSchedule(metaData.cron))
                        .build()
                scheduler.scheduleJob(jobDetail, trigger)
            } else {
                LOGGER.warn("类【${metaData.clazz}】不是QuartzJobBean的子类")
            }
        } catch (e: ClassNotFoundException) {
            LOGGER.error("无法获取类：${metaData.clazz}")
        }
    }

    /**
     * 批量添加任务
     */
    fun <T> addTask(vararg tasks: QuartzMetaData<T>) {
        tasks.forEach {
            this.addTask(it)
        }
    }

    /**
     * 批量添加任务
     */
    fun <T> addTask(tasks: Collection<QuartzMetaData<T>>) {
        tasks.forEach {
            this.addTask(it)
        }
    }

    /**
     * 移除定时任务
     * @throws Exception
     */
    @Throws(Exception::class)
    fun <T> removeTask(task: QuartzMetaData<T>): Boolean {
        val scheduler = this.schedulerFactoryBean.scheduler
        return scheduler.deleteJob(this.getJobKeyByTask(task))
    }

    /**
     * 移除定时任务
     */
    @Throws(Exception::class)
    fun <T> removeTask(taskList: List<QuartzMetaData<T>>): Boolean {
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
    fun <T> resumeTask(task: QuartzMetaData<T>) {
        val scheduler = this.schedulerFactoryBean.scheduler
        scheduler.resumeJob(this.getJobKeyByTask(task))
    }

    /**
     * 暂停定时任务
     * @param cloudTimedTask
     * @throws Exception
     */
    @Throws(Exception::class)
    fun <T> pauseTask(task: QuartzMetaData<T>) {
        val scheduler = this.schedulerFactoryBean.scheduler
        scheduler.pauseJob(this.getJobKeyByTask(task))
    }

    /**
     * 获取任务信息
     * @param cloudTimedTask
     * @return
     */
    @Throws(Exception::class)
    fun <T> getJob(task: QuartzMetaData<T>): JobDetail? {
        val scheduler = this.schedulerFactoryBean.scheduler
        return scheduler.getJobDetail(this.getJobKeyByTask(task))
    }


    /**
     * 根据任务信息获取jobkey
     * @param cloudTimedTask
     * @return
     */
    private fun <T> getJobKeyByTask(task: QuartzMetaData<T>): JobKey {
        return JobKey.jobKey(task.id)
    }
}