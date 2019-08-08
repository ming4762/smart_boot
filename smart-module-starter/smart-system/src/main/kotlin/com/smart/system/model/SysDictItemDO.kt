package com.smart.system.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

@TableName("sys_dict_item")
class SysDictItemDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var id: String? = null

    var itemCode: String? = null

    var itemValue: String? = null

    var seq: Int? = null

    var parentCode: String? = null

    var remark: String? = null

    var inUse: Boolean? = null

    var dictCode: String? = null

    var createUserId: String? = null

    var createTime: Date? = null

    var updateUserId: String? = null

    var updateTime: Date? = null
}