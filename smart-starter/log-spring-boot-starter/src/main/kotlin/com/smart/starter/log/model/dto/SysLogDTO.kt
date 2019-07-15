package com.smart.starter.log.model.dto

import com.smart.auth.common.model.SysUserDO
import com.smart.starter.log.model.SysLogDO

/**
 *
 * @author ming
 * 2019/7/15 下午2:19
 */
class SysLogDTO : SysLogDO() {

    /**
     * 用户
     */
    var user: SysUserDO? = null
}