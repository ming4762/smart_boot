package com.smart.system.mapper

import com.smart.starter.crud.mapper.CloudBaseMapper
import com.smart.system.model.SysMenuDO
import com.smart.system.model.vo.SysMenuVO

interface SysMenuMapper : CloudBaseMapper<SysMenuDO> {
    fun listWithFunction(parameters: Map<String, Any?>): List<SysMenuVO>?
}