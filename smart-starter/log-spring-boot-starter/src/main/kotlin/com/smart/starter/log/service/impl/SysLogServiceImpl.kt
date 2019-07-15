package com.smart.starter.log.service.impl

import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.smart.auth.common.service.AuthUserService
import com.smart.common.utils.BeanMapUtils
import com.smart.starter.crud.constants.CRUDConstants
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.starter.log.mapper.SysLogMapper
import com.smart.starter.log.model.SysLogDO
import com.smart.starter.log.model.dto.SysLogDTO
import com.smart.starter.log.service.SysLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 * @author ming
 * 2019/6/28 下午4:21
 */
@Service
class SysLogServiceImpl : BaseServiceImpl<SysLogMapper, SysLogDO>(), SysLogService {

    @Autowired
    private lateinit var authUserService: AuthUserService

    override fun list(queryWrapper: Wrapper<SysLogDO>, parameters: Map<String, Any?>, paging: Boolean): List<SysLogDO> {
        val list = super<BaseServiceImpl>.list(queryWrapper, parameters, paging)
        val withAll = parameters[CRUDConstants.WITH_ALL.name]
        if (withAll is Boolean && withAll) {
            return this.listWithAll(list)
        }
        return list
    }

    /**
     * 查询所有
     */
    private fun listWithAll(list: List<SysLogDO>): List<SysLogDO> {
        if (list.isEmpty()) return emptyList()
        val userIdList = list.mapNotNull { it.userId }
        if (userIdList.isEmpty()) return list
        // 查询人员信息
        val userMap = this.authUserService.listByIds(userIdList)
                .map { it.userId to it }
                .toMap()
        return list.map {
            val logDTO = BeanMapUtils.createFromParent(it, SysLogDTO :: class.java)
            logDTO.user = userMap[it.userId]
            return@map logDTO
        }
    }
}