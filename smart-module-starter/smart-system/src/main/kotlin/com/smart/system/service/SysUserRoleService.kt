package com.smart.system.service

import com.smart.starter.crud.service.BaseService
import com.smart.system.model.SysUserRoleDO

/**
 *
 * @author ming
 * 2019/7/1 下午4:46
 */
interface SysUserRoleService : BaseService<SysUserRoleDO> {

    fun queryByRoleIds(roleIdList: List<String>): Map<String, List<SysUserRoleDO>>

    fun queryByUserIds(userIdList: List<String>): Map<String, List<SysUserRoleDO>>
}