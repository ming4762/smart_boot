package com.smart.system.model

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel

/**
 *
 * @author ming
 * 2019/6/12 下午4:00
 */
@TableName("sys_user_role")
class SysUserRoleDO : BaseModel() {

    companion object {
        private const val serialVersionUID = 1560326471263
    }
    @TableId
    var userId: String? = null

    @TableId
    var roleId: String? = null

}