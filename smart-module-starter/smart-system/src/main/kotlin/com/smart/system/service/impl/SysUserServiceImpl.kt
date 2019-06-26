package com.smart.system.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.model.SysUserDO
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysRoleMapper
import com.smart.system.mapper.SysUserMapper
import com.smart.system.mapper.SysUserRoleMapper
import com.smart.system.model.SysRoleDO
import com.smart.system.model.SysUserRoleDO
import com.smart.system.service.SysUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 * @author ming
 * 2019/6/12 下午2:31
 */
@Service
class SysUserServiceImpl : BaseServiceImpl<SysUserMapper, SysUserDO>(), SysUserService {

    @Autowired
    private lateinit var sysUserRoleMapper: SysUserRoleMapper

    @Autowired
    private lateinit var sysRoleMapper: SysRoleMapper

    /**
     * 查询用户角色
     * TODO: 待测试
     */
    override fun queryUserRole(userIdList: List<String>): Map<String, List<SysRoleDO>> {
        if (userIdList.isEmpty()) return mapOf()
        // 查询角色ID
        val query = KtQueryWrapper(SysUserRoleDO())
        if (userIdList.size == 1) {
            query.eq(SysUserRoleDO :: userId, userIdList[0])
        } else {
            query.`in`(SysUserRoleDO :: userId, userIdList)
        }
        // 查询用户角色对应关系
        val sysUserRoleList = this.sysUserRoleMapper.selectList(query)
        val roleIdList = sysUserRoleList.map { it.roleId!! }
        if (roleIdList.isEmpty()) return mapOf()
        // 查询角色
        val roleList = this.sysRoleMapper.selectBatchIds(roleIdList)
        if (userIdList.size == 1) return mapOf(userIdList[0] to roleList)
        // 多个用户查询
        val roleMap = roleList.map { it.roleId to it }.toMap()
        val userRoleMap = sysUserRoleList.groupBy { it.userId!! }
        return userRoleMap.map {
            it.key to it.value.let { userRoleList ->
                val roleListNew = mutableListOf<SysRoleDO>()
                userRoleList.forEach { userRole ->
                    val role = roleMap[userRole.roleId]
                    if (role != null) roleListNew.add(role)
                }
                return@let roleListNew
            }
        }.toMap()
    }
}