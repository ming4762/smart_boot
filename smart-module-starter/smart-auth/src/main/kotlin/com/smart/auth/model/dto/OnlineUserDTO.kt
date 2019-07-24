package com.smart.auth.model.dto

import com.smart.auth.common.model.SysUserDO
import com.smart.auth.model.vo.SessionVO
import org.apache.shiro.session.Session
import java.io.Serializable
import java.util.*

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

    var session: SessionVO = SessionVO()

    constructor()
    constructor(user: SysUserDO, session: Session) {
        this.user = user
        this.session.sessionId = session.id
        this.session.startTime = session.startTimestamp
        this.session.lastAccessTime = session.lastAccessTime
        this.session.timeout = session.timeout
        this.session.host = session.host
        if (session.lastAccessTime != null) {
            val time = session.lastAccessTime.time - session.timeout
            this.session.lastUseTime = Date(time)
        }
    }
}