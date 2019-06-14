package com.smart.system.model

import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 *
 * @author ming
 * 2019/6/12 下午4:00
 */
class SysUserRoleDO : BaseModel() {

    companion object {
        private const val serialVersionUID = 1560326471263
    }

    var id: String? = null

    var userId: String? = null

    var roleId: String? = null

    var createUserId: String? = null

    var createTime: Date? = null

    var updateUserId: String? = null

    var updateTime: Date? = null
}