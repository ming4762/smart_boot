package com.smart.system.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 * 角色表
 * @author ming
 * 2019/6/12 下午3:20
 */
@TableName("sys_role")
open class SysRoleDO : BaseModel() {

    companion object {
        private const val serialVersionUID = 1560324063474L
    }

    @TableId(type = IdType.UUID)
    var roleId: String? = null

    var roleName: String? = null

    var remark: String? = null

    var createTime: Date? = null

    var roleType: String? = null

    var enable: Boolean = true

    var createUserId: String? = null

    var updateTime: Date? = null

    var updateUserId: String? = null

    var seq: Int? = null

}