package com.smart.system.model

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel

@TableName("sys_role_menu_function")
class SysRoleMenuFunctionDO : BaseModel() {

    companion object {
        public const val TYPE_MENU = "menu"
        public const val TYPE_FUNCTION = "function"
    }

    @TableId
    var roleId: String? = null

    @TableId
    var menuFunctionId: String? = null

    @TableId
    var type: String? = null

    @TableId
    var menuConfigId: String? = null

    var leaf: Boolean? = null
}