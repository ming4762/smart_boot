package com.smart.auth.model.dto

import com.smart.auth.common.model.SysUserDO
import java.io.Serializable

/**
 *
 * @author ming
 * 2019/7/23 下午3:51
 */
class OnlineUserDTO : Serializable {

    companion object {
        private const val serialVersionUID = 1563868334463L
    }

    lateinit var user: SysUserDO

    lateinit var sessionId: Serializable

    constructor()
    constructor(user: SysUserDO, sessionId: Serializable) {
        this.user = user
        this.sessionId = sessionId
    }
}