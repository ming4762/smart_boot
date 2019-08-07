package com.smart.portal.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 *
 * @author ming
 * 2019/8/1 下午3:39
 */
@TableName("smart-portal-pic-group")
class PortalPicGroupDO : BaseModel() {

    companion object {
        private const val serialVersionUID = 1564645233516L
    }

    @TableId(type = IdType.UUID)
    var groupId: String? = null

    var groupName: String? = null

    var seq: Int? = null

    var createTime: Date? = null
}