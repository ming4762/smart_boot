package com.smart.starter.kettle.service

/**
 *
 * @author ming
 * 2019/7/11 下午4:09
 */
interface KettleService {

    fun excuteDBTransfer(transName: String, directoryName: String? = null, params: Array<String> = arrayOf())

    fun excuteDBJob(name: String, directoryName: String? = null, params: Map<String, String>)
}