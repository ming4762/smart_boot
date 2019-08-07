package com.smart.portal.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 *
 * @author ming
 * 2019/8/1 下午3:43
 */
@TableName("smart-portal-pic")
class PortalPicDO : BaseModel() {
    companion object {
        private const val serialVersionUID = 1564645449652L
    }

    @TableId(type = IdType.UUID)
    var picId: String? = null

    var picName: String? = null

    var fileId: String? = null

    var createTime: Date? = null

    var groupId: String? = null
}