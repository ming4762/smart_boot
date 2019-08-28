package com.smart.portal.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 * 模块管理
 */
@TableName("smart_portal_module")
class PortalModuleDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var moduleId: String? = null

    var moduleCode: String? = null

    var moduleName: String? = null

    var moduleIcon: String? = null

    var coverPicId: String? = null

    var remark: String? = null

    var parentId: String? = null

    var topParentId: String? = null

    var seq: Int? = null

    var createTime: Date? = null

    var createUserId: String? = null

    var updateTime: Date? = null

    var updateUserId: String? = null
}