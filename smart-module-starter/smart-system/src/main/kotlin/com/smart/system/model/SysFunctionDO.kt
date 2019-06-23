package com.smart.system.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

@TableName("sys_function")
class SysFunctionDO : BaseModel() {

    companion object {
         private const val serialVersionUID = -264036880666859091L

        public const val CATALOG = "0"
        public const val MENU = "1"
        public const val BUTTON = "2"
    }

    /**
     * 功能下级
     */
    @TableField(exist = false)
    var children: List<SysFunctionDO>? = null

    /** FUNCTION_ID - 功能ID  */
    @TableId(type = IdType.UUID)
    var functionId: String? = null

    /** PARENT_ID - 上级ID  */
    @TableField("PARENT_ID")
    var parentId: String? = null

    /** FUNCTION_NAME - 功能名称  */
    @TableField("FUNCTION_NAME")
    var functionName: String? = null

    /** FUNCTION_TYPE - 功能类型  */
    @TableField("FUNCTION_TYPE")
    var functionType: String? = null

    /** ICON - 图标  */
    @TableField("ICON")
    var icon: String? = null

    /** SEQ - 序号  */
    @TableField("SEQ")
    var seq: Int? = null

    /** CREATE_TIME - 创建时间  */
    @TableField("CREATE_TIME")
    var createTime: Date? = null

    /** CREATE_USER_ID - 创建人员  */
    @TableField("CREATE_USER_ID")
    var createUserId: String? = null

    /** MODIFY_TIME - 修改时间  */
    var updateTime: Date? = null

    /** MODIFY_USER_ID - 修改人员  */
    var updateUserId: String? = null

    /** URL - URL  */
    var url: String? = null

    /** PREMISSION - 权限  */
    var premission: String? = null

    /** IS_MENU - 是否菜单  */
    @TableField("IS_MENU")
    var menuIs: Boolean? = null
}