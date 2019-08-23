package com.smart.file.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

@TableName("smart_file")
class SmartFileDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var fileId: String? = null

    var fileName: String? = null

    var createTime: Date? = null

    var createUserId: String? = null

    var type: String? = null

    var contentType: String? = null

    var size: Long? = null

    var dbId: String? = null

    var md5: String? = null

    var seq: Int? = null
}