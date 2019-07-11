package com.smart.starter.quartz.properties

/**
 * 定时任务元数据
 * @author ming
 * 2019/7/11 下午4:55
 */
class QuartzMetaData<T> {

    lateinit var clazz: String

    lateinit var cron: String

    lateinit var id: String

    var name: String = ""

    var data: T? = null
}