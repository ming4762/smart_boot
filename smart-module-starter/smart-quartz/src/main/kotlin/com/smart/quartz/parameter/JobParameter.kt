package com.smart.quartz.parameter

/**
 * 转换参数
 * @author ming
 * 2019/7/17 上午9:21
 */
class JobParameter : KettleParameter() {

    /**
     * JOB参数
     */
    var params: Map<String, String> = mapOf()
}