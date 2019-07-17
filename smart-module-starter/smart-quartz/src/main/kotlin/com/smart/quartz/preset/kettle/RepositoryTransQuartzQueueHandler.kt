package com.smart.quartz.preset.kettle

import com.alibaba.fastjson.JSON
import com.smart.quartz.parameter.TransParameter
import com.smart.starter.kettle.service.KettleService
import org.slf4j.LoggerFactory

/**
 * kettle资源库执行预设
 * @author ming
 * 2019/7/12 上午8:47
 */
class RepositoryTransQuartzQueueHandler : KettleBaseQuartzQueueHandler() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RepositoryTransQuartzQueueHandler::class.java)
    }

    /**
     * 执行kettle
     */
    override fun executeKettle(kettleService: KettleService, parameter: String) {
        try {
            val transParameter = JSON.parseObject(parameter, TransParameter :: class.java)
            kettleService.excuteDBTransfer(transParameter.name, transParameter.directory, transParameter.params)
        } catch (e: Exception) {
            LOGGER.error("执行转换失败", e)
        }
    }

}