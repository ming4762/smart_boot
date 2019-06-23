package com.smart.system.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

@TableName("sys_menu")
open class SysMenuDO : BaseModel() {

    companion object {
        private const val serialVersionUID = 2077003576466260943L
    }

    @TableId(type = IdType.UUID)
    var menuId: String? = null

    var menuName: String? = null

    var parentId: String? = null

    var icon: String? = null

    var seq: Int? = null

    var menuConfigId: String? = null

    var createTime: Date? = null

    var createUserId: String? = null

    var updateTime: Date? = null

    var updateUserId: String? = null

    var functionId: String? = null
}