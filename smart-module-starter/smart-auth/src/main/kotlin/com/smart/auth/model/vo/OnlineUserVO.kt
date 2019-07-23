package com.smart.auth.model.vo

import com.smart.auth.common.model.SysUserDO
import java.io.Serializable

/**
 *
 * @author ming
 * 2019/7/23 下午4:01
 */
class OnlineUserVO {

    lateinit var user: SysUserDO

    var onlineNum = 0

    lateinit var sessionIdList: List<Serializable>
}