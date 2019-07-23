package com.smart.auth.service

import com.smart.auth.model.vo.OnlineUserVO
import java.io.Serializable

/**
 *
 * @author ming
 * 2019/7/23 下午3:14
 */
interface AuthService {

    fun listOnlineUser(): List<OnlineUserVO>

    fun removeSession(sessionIdList: List<Serializable>): Int

    fun removeUser(userIdList: List<String>): Int
}