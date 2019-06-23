package com.smart.system.service

import com.smart.starter.crud.service.BaseService
import com.smart.system.model.SysRoleDO

/**
 *
 * @author ming
 * 2019/6/12 下午2:31
 */
interface SysUserService : BaseService<SysUserDO> {

    fun queryUserRole(userIdList: List<String>): Map<String, List<SysRoleDO>>
}