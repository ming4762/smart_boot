package com.smart.file.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 *
 * @author ming
 * 2019/9/7 下午4:37
 */
@TableName("smart_app_version")
class SmartAppVersionDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var versionId: String? = null

    var appId: String? = null

    var versionNumber: String? = null

    var description: String? = null

    var createTime: Date? = null

    var fileId: String? = null
}