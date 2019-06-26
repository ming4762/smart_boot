package com.smart.system.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.model.SysUserDO
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysRoleMapper
import com.smart.system.mapper.SysUserMapper
import com.smart.system.mapper.SysUserRoleMapper
import com.smart.system.model.SysRoleDO
import com.smart.system.model.SysUserRoleDO
import com.smart.system.service.SysRoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 * @author ming
 * 2019/6/12 下午3:49
 */
@Service
class SysRoleServiceImpl : BaseServiceImpl<SysRoleMapper, SysRoleDO>(), SysRoleService {

    @Autowired
    private lateinit var sysUserRoleMapper: SysUserRoleMapper

    @Autowired
    private lateinit var userMapper: SysUserMapper

    /**
     * 查询角色包含的人员
     * @param roleIdList 角色ID集合
     * TODO: 待测试
     */
    override fun queryRoleUser(roleIdList: List<String>): Map<String, List<SysUserDO>> {
        if (roleIdList.isEmpty()) return mapOf()
        val query = KtQueryWrapper(SysUserRoleDO())
        if (roleIdList.size == 1) {
            query.eq(SysUserRoleDO :: roleId, roleIdList[0])
        } else {
            query.`in`(SysUserRoleDO :: roleId, roleIdList)
        }
        // 查询用户角色对应关系
        val sysUserRoleList = this.sysUserRoleMapper.selectList(query)
        // 查询用户信息
        val userIdList = sysUserRoleList.map { it.userId!! }
        if (userIdList.isEmpty()) return mapOf()
        // 查询用户
        val userList = this.userMapper.selectBatchIds(userIdList)
        if (roleIdList.size == 1) return mapOf(roleIdList[0] to userList)

        // 多个角色查询
        // 将用户转为以ID为key的map
        val userMap = userList.map { it.userId!! to it }.toMap()
        return sysUserRoleList.groupBy { it.roleId!! }
                .map { it.key to it.value.let { userRoleList ->
                    val userListNew = mutableListOf<SysUserDO>()
                    userRoleList.forEach { userRole ->
                        val user = userMap[userRole.userId]
                        if (user != null) userListNew.add(user)
                    }
                    return@let userListNew
                } }.toMap()
    }
}