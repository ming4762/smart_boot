package com.smart.portal.model

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

@TableName("smart_portal_news_attachment")
class NewsAttachmentDO : BaseModel() {
    @TableId
    var newsId: String? = null

    @TableId
    var fileId: String? = null

    var seq: Int? = null

    var createTime: Date? = null
}