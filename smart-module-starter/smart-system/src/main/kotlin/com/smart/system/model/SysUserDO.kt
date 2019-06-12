package com.smart.system.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 * 系统用户实体
 * @author ming
 * 2019/6/12 下午2:18
 */
@TableName("sys_user")
class SysUserDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var userId: String? = null

    var username: String? = null

    var name: String? = null

    var password: String? = null

    var deptId: String? = null

    var email: String? = null

    var telephone: String? = null

    var status: String? = null

    var userType: String? = null

    var sex: String? = null

    var birth: Date? = null

    var createUserId: String? = null

    var createTime: Date? = null

    var updateUserId: String? = null

    var updateTime: Date? = null

    var avatarId: String? = null

    var seq: Int? = null
}