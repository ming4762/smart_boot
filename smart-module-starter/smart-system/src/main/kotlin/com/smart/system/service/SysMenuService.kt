package com.smart.system.service

import com.smart.common.model.Tree
import com.smart.starter.crud.service.BaseService
import com.smart.system.model.SysMenuDO
import com.smart.system.model.vo.SysMenuVO

interface SysMenuService : BaseService<SysMenuDO> {
    fun saveChildrenMenu(parameters: Map<String, Any?>): Boolean?
    abstract fun listWithFunction(parameters: Map<String, Any?>): List<SysMenuVO>?
    abstract fun queryUserMenu(userId: String): List<Tree<SysMenuVO>>?
}