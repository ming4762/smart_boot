package com.smart.starter.kettle.service

/**
 *
 * @author ming
 * 2019/7/11 下午4:09
 */
interface KettleService {

    fun excuteDBTransfer(transName: String, params: Array<String> = arrayOf())
}