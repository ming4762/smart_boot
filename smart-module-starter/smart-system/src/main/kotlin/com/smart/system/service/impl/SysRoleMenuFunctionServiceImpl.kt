package com.smart.system.service.impl

import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysRoleMenuFunctionMapper
import com.smart.system.model.SysRoleMenuFunctionDO
import com.smart.system.service.SysRoleMenuFunctionService
import org.springframework.stereotype.Service

/**
 *
 * @author ming
 * 2019/7/2 下午1:31
 */
@Service
class SysRoleMenuFunctionServiceImpl : BaseServiceImpl<SysRoleMenuFunctionMapper, SysRoleMenuFunctionDO>(), SysRoleMenuFunctionService {
}