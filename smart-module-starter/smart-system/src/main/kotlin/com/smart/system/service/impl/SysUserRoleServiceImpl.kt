package com.smart.system.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysUserRoleMapper
import com.smart.system.model.SysUserRoleDO
import com.smart.system.service.SysUserRoleService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors
import kotlin.reflect.KMutableProperty1

/**
 *
 * @author ming
 * 2019/7/1 下午4:47
 */
@Service
class SysUserRoleServiceImpl : BaseServiceImpl<SysUserRoleMapper, SysUserRoleDO>(), SysUserRoleService {

    override fun queryByRoleIds(roleIdList: List<String>): Map<String, List<SysUserRoleDO>> {
        return this.queryByIds(roleIdList, SysUserRoleDO :: roleId)
    }

    override fun queryByUserIds(userIdList: List<String>): Map<String, List<SysUserRoleDO>> {
        return this.queryByIds(userIdList, SysUserRoleDO :: userId)
    }

    private fun queryByIds(idList: List<String>, kMutableProperty1: KMutableProperty1<SysUserRoleDO, String?>): Map<String, List<SysUserRoleDO>> {
        if (idList.isEmpty()) return mapOf()
        if (idList.size == 1) {
            return mapOf(
                    idList[0] to this.list(KtQueryWrapper(SysUserRoleDO::class.java).eq(kMutableProperty1, idList[0]))
            )
        }
        return this.list(
                KtQueryWrapper(SysUserRoleDO::class.java).`in`(kMutableProperty1, idList)
        ).stream().collect(Collectors.groupingBy(kMutableProperty1))
                .map { it.key!! to it.value }
                .toMap()
    }

    @Transactional
    override fun saveOrUpdate(entity: SysUserRoleDO?): Boolean {
        return super.saveOrUpdate(entity)
    }

    @Transactional
    override fun batchDelete(tList: List<SysUserRoleDO>): Int {
        return super.batchDelete(tList)
    }
}