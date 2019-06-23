package com.smart.system.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 * 菜单配置
 * @author ming
 * 2019/6/22 下午9:11
 */
@TableName("sys_menu_config")
class SysMenuConfigDO : BaseModel() {

    companion object {
        // 默认配置的ID
        public const val DEFAULT_CONFIG_ID = "0"
    }

    @TableId(type = IdType.UUID)
    var configId: String? = null

    @TableField
    var configName: String? = null

    @TableField
    var status: Boolean? = null

    @TableField
    var userId: String? = null

    @TableField
    var createTime: Date? = null

    @TableField
    var createUserId: String? = null

    @TableField
    var seq: Int? = null
}