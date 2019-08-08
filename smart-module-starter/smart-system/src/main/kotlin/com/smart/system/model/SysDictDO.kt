package com.smart.system.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

@TableName("sys_dict")
class SysDictDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var dictCode: String? = null

    var dictName: String? = null

    var createUserId: String? = null

    var createTime: Date? = null

    var remark: String? = null

    var inUse: Boolean? = null

    var updateUserId: String? = null

    var updateTime: Date? = null

    var seq: Int? = null
}