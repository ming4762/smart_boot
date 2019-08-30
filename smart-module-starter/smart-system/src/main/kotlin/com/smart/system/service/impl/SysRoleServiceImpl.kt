package com.smart.system.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.model.SysUserDO
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.model.Tree
import com.smart.common.utils.BeanMapUtils
import com.smart.common.utils.TreeUtils
import com.smart.starter.crud.constants.CRUDConstants
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysRoleMapper
import com.smart.system.mapper.SysUserRoleMapper
import com.smart.system.model.SysRoleDO
import com.smart.system.model.SysRoleMenuFunctionDO
import com.smart.system.model.SysUserRoleDO
import com.smart.system.model.dto.SysRoleDTO
import com.smart.system.model.vo.SysRoleVO
import com.smart.system.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

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
    private lateinit var userService: SysUserService

    @Autowired
    private lateinit var organService: SysOrganService

    @Autowired
    private lateinit var userRoleService: SysUserRoleService

    @Autowired
    private lateinit var roleMenuFunctionService: SysRoleMenuFunctionService

    override fun list(queryWrapper: QueryWrapper<SysRoleDO>, parameters: Map<String, Any?>, paging: Boolean): List<SysRoleDO> {
        val list = super<BaseServiceImpl>.list(queryWrapper, parameters, paging)
        val withAll = parameters[CRUDConstants.WITH_ALL.name]
        if (withAll is Boolean && withAll == true) {
            return this.listWithAll(list)
        }
        return list
    }

    /**
     * 重写查询详情信息
     */
    override fun queryDetail(t: SysRoleDO): SysRoleDO? {
        val role = this.get(t)
        if (role != null) {
            val roleDTO = BeanMapUtils.createFromParent(role, SysRoleDTO :: class.java)
            roleDTO.organ = role.organId?.let {
                return@let this.organService.getById(it)
            }
            roleDTO.createUser = role.createUserId?.let {
                return@let this.userService.getById(it)
            }
            roleDTO.updateUser = role.updateUserId?.let {
                return@let this.userService.getById(it)
            }
            return roleDTO
        }
        return null
    }

    /**
     * 重写保存修改操作
     */
    override fun saveOrUpdate(entity: SysRoleDO): Boolean {
        var isAdd = false
        if (entity.roleId == null) {
            isAdd = true
        } else if (this.getById(entity.roleId) == null) {
            isAdd = true
        }
        if (isAdd) {
            entity.createTime = Date()
            entity.createUserId = AuthUtils.getCurrentUserId()
            return this.save(entity)
        } else {
            entity.updateTime = Date()
            entity.updateUserId = AuthUtils.getCurrentUserId()
            return this.updateById(entity)
        }
    }

    /**
     * 重写删除方法
     */
    @Transactional
    override fun delete(t: SysRoleDO): Int {
        // 删除角色人员关系
        this.userRoleService.remove(
                KtQueryWrapper(SysUserRoleDO::class.java).eq(SysUserRoleDO :: roleId, t.roleId)
        )
        // TODO:删除角色功能关系
        // 删除角色
        return this.baseMapper.deleteById(t.roleId)
    }

    /**
     * 查询所有
     */
    private fun listWithAll(list: List<SysRoleDO>): List<SysRoleVO> {
        // 查询人员信息
        if (list.isEmpty()) return emptyList()
        val userIdList = mutableListOf<String>()
        userIdList.addAll(list.mapNotNull { it.createUserId })
        userIdList.addAll(list.mapNotNull { it.updateUserId })
        val idList = userIdList.distinct()
        val userMap = if (idList.isEmpty()) mapOf() else this.userService.listByIds(idList)
                .map { it.userId!! to it }.toMap()
        return list.map {
            val roleVO = BeanMapUtils.createFromParent(it, SysRoleVO :: class.java)
            roleVO.createUser = userMap[it.createUserId]
            roleVO.updateUser = userMap[it.updateUserId]
            return@map roleVO
        }
    }

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
        val userIdList = sysUserRoleList.map { it.userId!! }.distinct()
        if (userIdList.isEmpty()) return mapOf()
        // 查询用户
        val userList = this.userService.listByIds(userIdList)
        if (roleIdList.size == 1) return mapOf(roleIdList[0] to userList.toMutableList())

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

    /**
     * 查询所有角色带有组织结构
     * TODO: 删除组织结构需要把organ_id设置为null
     */
    override fun listAllTreeWithOrgan(): Tree<Any>? {
        // 查询 所有角色
        val roleList = this.list()
        // 查询组织结构ID
        val noOrganList = mutableListOf<SysRoleDO>()
        val hasOrganList = mutableSetOf<SysRoleDO>()
        roleList.forEach {
            if (it.organId == null) {
                noOrganList.add(it)
            } else {
                hasOrganList.add(it)
            }
        }
        // 以组织结构进行分组
        val organRoleMap = hasOrganList.groupBy { it.organId!! }
        // 查询组织机构
        val organList = this.organService.list()
                .map {
                    val tree = Tree<Any>()
                    tree.id = it.organId
                    tree.text = it.organName
                    tree.parentId = it.parentId
                    tree.`object` = it
                    tree.attributes = mapOf("type" to "organ")
                    return@map tree
                }
        // 构建组织机构数
        val organTreeList = TreeUtils.buildList(organList, "0")
        // 组织结构树添加角色信息
        this.addRoleToOrganTree(organTreeList, organRoleMap)
        val resultTree = Tree<Any>()
        resultTree.id = "0"
        resultTree.text = "角色根"
        resultTree.children.addAll(organTreeList)
        resultTree.children.addAll(noOrganList.map {
            return@map this.roleToTree(it)
        })
        return resultTree
    }

    /**
     * 更新人员信息
     */
    override fun updateUser(roleToUserIdList: Map<String, List<String>>): Boolean {
        // 删除人员角色关系
        val roleIdSet = roleToUserIdList.keys
        val deleteWrapper = KtQueryWrapper(SysUserRoleDO :: class.java)
        deleteWrapper.`in`(SysUserRoleDO :: roleId, roleIdSet)
        val result = this.userRoleService.remove(deleteWrapper)
        // 添加人员角色关系
        val userRoleList = mutableListOf<SysUserRoleDO>()
        roleToUserIdList.forEach { roleId, userIdList ->
            userIdList.forEach {
                val userRoleDO = SysUserRoleDO()
                userRoleDO.roleId = roleId
                userRoleDO.userId = it
                userRoleList.add(userRoleDO)
            }
        }
        val resultInsert = this.userRoleService.saveBatch(userRoleList)
        return result && resultInsert
    }

    /**
     * 角色授权
     */
    @Transactional(rollbackFor = [Exception :: class])
    override fun authorize(list: List<SysRoleMenuFunctionDO>): Boolean {
        if (!list.isEmpty()) {
            val roleMenuFunction = list[0]
            // 删除对应角色的权限
            // 构造删除条件
            val deleteWrapper = KtQueryWrapper(SysRoleMenuFunctionDO :: class.java)
            deleteWrapper.eq(SysRoleMenuFunctionDO :: roleId, roleMenuFunction.roleId)
                    .eq(SysRoleMenuFunctionDO :: menuConfigId, roleMenuFunction.menuConfigId)
            val deleteResult = this.roleMenuFunctionService.remove(deleteWrapper)
            // 保存对应角色的权限
            val saveResult = this.roleMenuFunctionService.saveBatch(list)
            return deleteResult && saveResult
        }
        return true
    }

    private fun addRoleToOrganTree(organTreeList: List<Tree<Any>>, organRoleMap: Map<String, List<SysRoleDO>>) {
        organTreeList.forEach {
            // 将该组织包含的角色添加到下级
            val roleList = organRoleMap[it.id]
            if (roleList != null) {
                it.children.addAll(roleList.map {
                    return@map this.roleToTree(it)
                })
            }
            // 遍历下级
            if (it.isHasChildren) {
                if (roleList == null || roleList.isEmpty()) {
                    this.addRoleToOrganTree(it.children, organRoleMap)
                } else {
                    this.addRoleToOrganTree(it.children.subList(0, it.children.size - roleList.size), organRoleMap)
                }
            }
         }
    }

    private fun roleToTree(role: SysRoleDO): Tree<Any> {
        val tree = Tree<Any>()
        tree.id = role.roleId
        tree.text = role.roleName
        tree.`object` = role
        tree.attributes = mapOf("type" to "role")
        return tree
    }
}