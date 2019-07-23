package com.smart.auth.service.impl

import com.smart.auth.common.model.SysUserDO
import com.smart.auth.model.vo.OnlineUserVO
import com.smart.auth.model.dto.OnlineUserDTO
import com.smart.auth.service.AuthService
import org.apache.shiro.session.mgt.eis.SessionDAO
import org.apache.shiro.subject.SimplePrincipalCollection
import org.apache.shiro.subject.support.DefaultSubjectContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
        return onlineUserList.groupBy { it.user.userId!! }
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
}