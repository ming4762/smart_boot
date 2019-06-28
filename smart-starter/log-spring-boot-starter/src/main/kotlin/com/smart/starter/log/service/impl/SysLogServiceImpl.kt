package com.smart.starter.log.service.impl

import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.starter.log.mapper.SysLogMapper
import com.smart.starter.log.model.SysLogDO
import com.smart.starter.log.service.SysLogService
import org.springframework.stereotype.Service

/**
 *
 * @author ming
 * 2019/6/28 下午4:21
 */
@Service
class SysLogServiceImpl : BaseServiceImpl<SysLogMapper, SysLogDO>(), SysLogService {
}