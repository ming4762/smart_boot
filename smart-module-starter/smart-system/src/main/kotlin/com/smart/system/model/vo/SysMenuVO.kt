package com.smart.system.model.vo

import com.smart.system.model.SysFunctionDO
import com.smart.system.model.SysMenuDO

/**
 * menuVo
 */
class SysMenuVO: SysMenuDO() {

    /**
     * 菜单对应的功能
     */
    var function: SysFunctionDO? = null

    // 菜单地址
    var url: String? = null
}