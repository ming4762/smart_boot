package com.smart.portal.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 * 图文素材实体类
 * @author ming
 * 2019/8/1 下午2:43
 */
@TableName("smart_portal_material_gt")
class PortalMaterialGtDO : BaseModel() {

    companion object {
        private const val serialVersionUID = 1564645245202L
    }

    @TableId(type = IdType.UUID)
    var materialId: String? = null

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
}