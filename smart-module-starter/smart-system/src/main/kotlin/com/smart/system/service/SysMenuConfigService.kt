package com.smart.system.service

import com.smart.starter.crud.service.BaseService
import com.smart.system.model.SysMenuConfigDO

interface SysMenuConfigService : BaseService<SysMenuConfigDO> {
    // 激活配置
    abstract fun activeConfig(menuConfig: SysMenuConfigDO): Boolean

    /**
     * 查询人员的菜单配置信息
     */
    fun queryUserMenuConfig(userIdList: List<String>): Map<String, SysMenuConfigDO>
}