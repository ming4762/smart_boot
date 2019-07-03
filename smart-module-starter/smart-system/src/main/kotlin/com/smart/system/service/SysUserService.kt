package com.smart.system.service

import com.smart.auth.common.model.SysUserDO
import com.smart.starter.crud.service.BaseService
import com.smart.system.model.SysRoleDO

/**
 *
 * @author ming
 * 2019/6/12 下午2:31
 */
interface SysUserService : BaseService<SysUserDO> {

    fun queryUserRole(userIdList: List<String>): Map<String, List<SysRoleDO>>

    /**
     * 查询用户权限信息
     */
    fun queryPermissionList(userIdList: List<String>): Map<String, Set<String>>
}