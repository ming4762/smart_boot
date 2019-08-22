package com.smart.portal.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

@TableName("smart_portal_news")
class NewsDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var newsId: String? = null

    var title: String? = null

    var subtitle: String? = null

    var summary: String? = null

    var coverPicId: String? = null

    var author: String? = null

    var releaseTime: Date? = null

    var createUserId: String? = null

    var createTime: Date? = null

    var updateUserId: String? = null

    var updateTime: Date? = null

    var content: String? = null

    var moduleId: String? = null

    var praiseNum: Int? = null

    var readNum: Int? = null

    var commentNum: Int? = null

    var top: Boolean = false

    var sources: String? = null
}