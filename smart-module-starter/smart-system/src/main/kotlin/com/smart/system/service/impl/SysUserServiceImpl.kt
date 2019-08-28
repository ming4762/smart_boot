package com.smart.system.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.model.SysUserDO
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.utils.security.MD5Utils
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysRoleMapper
import com.smart.system.mapper.SysUserMapper
import com.smart.system.mapper.SysUserRoleMapper
import com.smart.system.model.SysRoleDO
import com.smart.system.model.SysRoleMenuFunctionDO
import com.smart.system.model.SysUserRoleDO
import com.smart.system.service.SysFunctionService
import com.smart.system.service.SysMenuConfigService
import com.smart.system.service.SysRoleMenuFunctionService
import com.smart.system.service.SysUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.util.*

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

    @Autowired
    private lateinit var menuConfigService: SysMenuConfigService

    @Autowired
    private lateinit var roleMenuFunctionService: SysRoleMenuFunctionService

    @Autowired
    private lateinit var functionService: SysFunctionService

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

    /**
     * 查询用户权限信息
     * TODO: 单用户ID测试成功，多用户ID未测试
     */
    override fun queryPermissionList(userIdList: List<String>): Map<String, Set<String>> {
        if (userIdList.isEmpty()) return mapOf()
        // 查询人员的菜单配置信息
        val userMenuConfigMap = this.menuConfigService.queryUserMenuConfig(userIdList)
        // 查询人员的角色信息
        val userRoleWrapper = KtQueryWrapper(SysUserRoleDO :: class.java)
        if (userIdList.size == 1) userRoleWrapper.eq(SysUserRoleDO :: userId, userIdList[0]) else userRoleWrapper.`in`(SysUserRoleDO :: userId, userIdList)
        val userRoleMap = this.sysUserRoleMapper.selectList(userRoleWrapper)
                .groupBy { it.userId!! }
                .map { it.key to it.value.map { it.roleId!! } }
                .toMap()
        val roleIdList = mutableSetOf<String>()
        userRoleMap.values.forEach {
            roleIdList.addAll(it)
        }
        val menuConfigIdList = userMenuConfigMap.values.map { it.configId!! }.toSet()
        if (roleIdList.isNotEmpty()) {
            // 通过角色、菜单配置查询功能信息
            val wrapper = KtQueryWrapper(SysRoleMenuFunctionDO :: class.java)
                    .eq(SysRoleMenuFunctionDO::type, SysRoleMenuFunctionDO.TYPE_FUNCTION)
            if (menuConfigIdList.size ==1) {
                wrapper.eq(SysRoleMenuFunctionDO :: menuConfigId, menuConfigIdList.first())
            } else {
                wrapper.`in`(SysRoleMenuFunctionDO :: menuConfigId, menuConfigIdList)
            }
            // 设置角色ID条件
            if (roleIdList.size == 1) {
                wrapper.eq(SysRoleMenuFunctionDO :: roleId, roleIdList.first())
            } else {
                wrapper.`in`(SysRoleMenuFunctionDO :: roleId, roleIdList)
            }
            // 执行查询
            val roleMenuFunctionMap = this.roleMenuFunctionService.list(wrapper)
                    .groupBy { "${it.menuConfigId}${it.roleId}" }
                    .map { it.key to it.value.map { it.menuFunctionId!! } }
                    .toMap()
            // 查询功能对应的功能权限
            val functionIdList = mutableListOf<String>()
            roleMenuFunctionMap.values.forEach {
                functionIdList.addAll(it)
            }
            val functionList = if (functionIdList.isEmpty()) listOf() else this.functionService.listByIds(functionIdList)
            val fuctionIdPermissionMap = mutableMapOf<String, String>()
            functionList.forEach {
                if (!StringUtils.isEmpty(it.premission)) {
                    fuctionIdPermissionMap[it.functionId!!] = it.premission!!
                }
            }
            // 获取configId roleId to permission
            val roleMenuPermissionMap = roleMenuFunctionMap.map {
                it.key to it.value.mapNotNull { functionId ->
                    fuctionIdPermissionMap[functionId]
                }
            }.toMap()
            // 获取人员与configId，roleId关系
            val userToConfigIdRoleIdMap = userRoleMap.map {
                val configId = userMenuConfigMap.getValue(it.key).configId!!
                val permissionSet = mutableSetOf<String>()
                it.value.forEach { roleId ->
                    permissionSet.addAll(roleMenuPermissionMap["$configId$roleId"] ?: setOf())
                }
                it.key to permissionSet
            }.toMap()
            return userToConfigIdRoleIdMap
        }
        return mapOf()
    }

    @Transactional
    override fun batchDelete(tList: List<SysUserDO>): Int {
        return super.batchDelete(tList)
    }

    /**
     * 批量保存更新
     */
    @Transactional
    override fun saveOrUpdateBatch(entityList: MutableCollection<SysUserDO>): Boolean {
        val result = entityList.map {
            return@map this.saveOrUpdate(it)
        }
        return result.all { it }
    }

    @Transactional
    override fun saveOrUpdate(entity: SysUserDO): Boolean {
        var isAdd = false
        if (entity.userId == null) {
            isAdd = true
        } else if (this.getById(entity.userId) == null) {
            isAdd = true
        }
        return if (isAdd) {
            entity.createTime = Date()
            entity.createUserId = AuthUtils.getCurrentUserId()
            if (entity.password == null) {
                entity.password = this.createDefaultPassword(entity)
            }
            this.save(entity)
        } else {
            entity.updateTime = Date()
            entity.updateUserId = AuthUtils.getCurrentUserId()
            this.updateById(entity)
        }
    }

    /**
     * 创建默认的密码
     */
    private fun createDefaultPassword(user: SysUserDO): String {
        val password = user.username + "123456"
        return MD5Utils.md5(user.username + password + SALT, 2)
    }

    companion object {
        private const val SALT = "1qazxsw2"
    }
}