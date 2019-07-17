package com.smart.quartz.preset.kettle

import com.alibaba.fastjson.JSON
import com.smart.quartz.parameter.JobParameter
import com.smart.starter.kettle.service.KettleService
import org.slf4j.LoggerFactory

/**
 * 执行job预设类
 * @author ming
 * 2019/7/17 上午9:13
 */
class RepositoryJobQuartzQueueHandler : KettleBaseQuartzQueueHandler() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RepositoryJobQuartzQueueHandler::class.java)
    }

    /**
     * 执行转换
     */
    override fun executeKettle(kettleService: KettleService, parameter: String) {
        try {
            val jobParameter = JSON.parseObject(parameter, JobParameter :: class.java)
            kettleService.excuteDBJob(jobParameter.name, jobParameter.directory, jobParameter.params)
        } catch (e: Exception) {
            LOGGER.error("执行job失败", e)
        }
    }

}