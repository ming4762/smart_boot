package com.smart.auth.service.impl

import com.smart.auth.common.model.SysUserDO
import com.smart.auth.model.dto.OnlineUserDTO
import com.smart.auth.model.vo.OnlineUserVO
import com.smart.auth.service.AuthService
import org.apache.shiro.session.mgt.eis.SessionDAO
import org.apache.shiro.subject.SimplePrincipalCollection
import org.apache.shiro.subject.support.DefaultSubjectContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.Serializable

/**
 *
 * @author ming
 * 2019/7/23 下午3:16
 */
@Service
class AuthServiceImpl : AuthService {

    @Autowired
    private lateinit var sessionDAO: SessionDAO

    /**
     * 获取所有在线用户
     */
    override fun listOnlineUser(): List<OnlineUserVO> {
        return this.getOnlineUser().groupBy { it.user.userId!! }
                .map {
                    val onlineUserVO = OnlineUserVO()
                    onlineUserVO.user = it.value.first().user
                    onlineUserVO.onlineNum = it.value.size
                    onlineUserVO.sessionIdList = it.value.map { onlineUserDTO ->
                        onlineUserDTO.sessionId
                    }
                    return@map onlineUserVO
                }
    }

    /**
     * 移除session
     */
    override fun removeSession(sessionIdList: List<Serializable>): Int {
        var num = 0
        sessionIdList.forEach {
            val session = this.sessionDAO.readSession(it)
            if (session != null) {
                num ++
                this.sessionDAO.delete(session)
            }
        }
        return num
    }

    /**
     * 移除user
     */
    override fun removeUser(userIdList: List<String>): Int {
        val onlineUserList = this.getOnlineUser(userIdList)
        return this.removeSession(onlineUserList.map { it.sessionId }.distinct())
    }

    /**
     * 获取在线用户
     */
    private fun getOnlineUser(userIdList: List<String> = listOf()): List<OnlineUserDTO> {
        val activeSessions = sessionDAO.activeSessions
        val onlineUserList = mutableListOf<OnlineUserDTO>()
        // 遍历session获取用户信息
        for (session in activeSessions) {
            if (session?.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY) == null) continue
            val principalCollection = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) as SimplePrincipalCollection
            val user = principalCollection.primaryPrincipal as SysUserDO
            val onlineUser = OnlineUserDTO(user, session.id)
            onlineUserList.add(onlineUser)
        }
        return if (userIdList.isEmpty()) {
            onlineUserList
        } else {
            onlineUserList.filter { userIdList.contains(it.user.userId) }
        }
    }
}