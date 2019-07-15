package com.smart.quartz.preset

/**
 * 存储定时任务预设
 * @author ming
 * 2019/7/12 上午10:46
 */
enum class PresetClass(var value: String, var clazz: String) {
    // 执行资源库kettle
    KETTLE_REPOSITORY_TRANS("资源库kettle","com.smart.quartz.preset.kettle.RepositoryTransQuartzQueueHandler")
}