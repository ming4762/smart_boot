package com.smart.system.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 * 组织机构
 * @author ming
 * 2019/6/26 下午3:57
 */
@TableName("sys_organ")
class SysOrganDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var organId: String? = null
    var organCode: String? = null
    var organName: String? = null
    var shortName: String? = null
    var leaderId: String? = null
    var description: String? = null
    var parentId: String? = null

    var createTime: Date? = null
    var createUserId: String? = null

    var updateTime: Date? = null

    var updateUserId: String? = null

    var topParentId: String? = null
}