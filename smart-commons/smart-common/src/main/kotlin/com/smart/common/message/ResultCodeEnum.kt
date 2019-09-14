package com.smart.common.message

/**
 *
 * @author ming
 * 2019/6/12 上午10:12
 */
enum class ResultCodeEnum constructor(val code: Int, val msg: String) {
    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    /**
     * 失败
     */
    FAILURE(500, "失败")

}