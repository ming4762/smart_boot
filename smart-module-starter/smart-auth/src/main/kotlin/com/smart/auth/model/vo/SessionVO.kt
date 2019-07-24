package com.smart.auth.model.vo

import java.io.Serializable
import java.util.*

/**
 *
 * @author ming
 * 2019/7/24 上午8:43
 */
class SessionVO {

    lateinit var sessionId: Serializable

    lateinit var startTime: Date

    var lastAccessTime: Date? = null

    var timeout: Long? = null

    var lastUseTime: Date? = null

    var host: String = "127.0.0.1"
}