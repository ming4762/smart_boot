package com.smart.system.model.vo

import com.smart.auth.common.model.SysUserDO
import com.smart.system.model.SysRoleDO

/**
 *
 * @author ming
 * 2019/6/27 上午10:32
 */
class SysRoleVO : SysRoleDO() {

    var createUser: SysUserDO? = null

    var updateUser: SysUserDO? = null
}