package com.smart.auth.service

import com.smart.auth.model.vo.OnlineUserVO

/**
 *
 * @author ming
 * 2019/7/23 下午3:14
 */
interface AuthService {

    fun listOnlineUser(): List<OnlineUserVO>
}