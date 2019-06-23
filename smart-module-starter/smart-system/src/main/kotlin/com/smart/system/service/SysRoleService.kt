package com.smart.system.service

import com.smart.starter.crud.service.BaseService
import com.smart.system.model.SysRoleDO

/**
 *
 * @author ming
 * 2019/6/12 下午3:48
 */
interface SysRoleService : BaseService<SysRoleDO> {

    fun queryRoleUser(roleIdList: List<String>): Map<String, List<SysUserDO>>
}