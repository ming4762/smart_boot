package com.smart.system.model.dto

import com.smart.auth.common.model.SysUserDO
import com.smart.system.model.SysOrganDO
import com.smart.system.model.SysRoleDO

/**
 *
 * @author ming
 * 2019/7/1 下午3:55
 */
class SysRoleDTO : SysRoleDO() {

    var createUser: SysUserDO? = null

    var updateUser: SysUserDO? = null

    var organ: SysOrganDO? = null
}