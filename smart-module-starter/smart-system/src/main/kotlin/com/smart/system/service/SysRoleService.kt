package com.smart.system.service

import com.smart.auth.common.model.SysUserDO
import com.smart.common.model.Tree
import com.smart.starter.crud.service.BaseService
import com.smart.system.model.SysRoleDO
import com.smart.system.model.SysRoleMenuFunctionDO

/**
 *
 * @author ming
 * 2019/6/12 下午3:48
 */
interface SysRoleService : BaseService<SysRoleDO> {

    fun queryRoleUser(roleIdList: List<String>): Map<String, List<SysUserDO>>
    abstract fun listAllTreeWithOrgan(): Tree<Any>?
    abstract fun updateUser(roleToUserIdList: Map<String, List<String>>): Boolean
    abstract fun authorize(list: List<SysRoleMenuFunctionDO>): Boolean
}