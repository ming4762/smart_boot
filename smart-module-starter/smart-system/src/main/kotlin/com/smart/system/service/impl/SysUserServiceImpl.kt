package com.smart.system.service.impl

import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysUserMapper
import com.smart.system.model.SysUserDO
import com.smart.system.service.SysUserService
import org.springframework.stereotype.Service

/**
 *
 * @author ming
 * 2019/6/12 下午2:31
 */
@Service
class SysUserServiceImpl : BaseServiceImpl<SysUserMapper, SysUserDO>(), SysUserService {
}