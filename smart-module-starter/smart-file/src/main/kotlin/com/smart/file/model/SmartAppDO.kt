package com.smart.file.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 * APP信息
 * @author ming
 * 2019/9/7 下午4:27
 */
@TableName("smart_app")
class SmartAppDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var appId: String ? = null

    var appName: String ? = null

    var appCode: String ? = null

    var developer: String ? = null

    var type: String ? = null

    var description: String ? = null

    var createTime: Date ? = null
}