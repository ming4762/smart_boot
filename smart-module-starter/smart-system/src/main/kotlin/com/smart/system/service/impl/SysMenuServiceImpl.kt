package com.smart.system.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.model.Tree
import com.smart.common.utils.BeanMapUtils
import com.smart.common.utils.TreeUtils
import com.smart.common.utils.UUIDGenerator
import com.smart.starter.crud.query.QueryParameter
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.starter.crud.utils.MybatisUtil
import com.smart.system.mapper.SysMenuMapper
import com.smart.system.mapper.SysRoleMenuFunctionMapper
import com.smart.system.mapper.SysUserRoleMapper
import com.smart.system.model.SysFunctionDO
import com.smart.system.model.SysMenuDO
import com.smart.system.model.SysRoleMenuFunctionDO
import com.smart.system.model.SysUserRoleDO
import com.smart.system.model.vo.SysMenuVO
import com.smart.system.service.SysFunctionService
import com.smart.system.service.SysMenuConfigService
import com.smart.system.service.SysMenuService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*
import java.util.stream.Collectors

/**
 * 菜单服务层
 * @author zhongming
 */
@Service
class SysMenuServiceImpl : BaseServiceImpl<SysMenuMapper, SysMenuDO>(), SysMenuService {



    @Autowired
    private lateinit var functionService: SysFunctionService

    @Autowired
    private lateinit var menuConfigService: SysMenuConfigService

    @Autowired
    private lateinit var userRoleMapper: SysUserRoleMapper

    @Autowired
    private lateinit var roleMenuFunctionMapper: SysRoleMenuFunctionMapper

    /**
     * 查询用户菜单
     */
    override fun queryUserMenu(userId: String): List<Tree<SysMenuVO>>? {
//        userId = "1"
        // 查询人员的配置信息，如果人员没有菜单配置，则使用系统菜单配置
        val configId = this.menuConfigService.queryUserMenuConfig(arrayListOf(userId)).getValue(userId).configId
        if (configId != null) {
            // 查询人员对应角色ID列表
            val roleWrapper = KtQueryWrapper(SysUserRoleDO :: class.java)
            roleWrapper.eq(SysUserRoleDO :: userId, userId)
            val roleIdList = this.userRoleMapper.selectList(roleWrapper)?.map { it.roleId!! }
            // 根据角色、菜单配置查询菜单信息
            if (roleIdList != null && roleIdList.isNotEmpty()) {
                val roleMenuWrapper = KtQueryWrapper(SysRoleMenuFunctionDO :: class.java).eq(SysRoleMenuFunctionDO :: menuConfigId, configId)
                        .`in`(SysRoleMenuFunctionDO :: roleId, roleIdList)
                        .eq(SysRoleMenuFunctionDO :: type, "menu")
                val menuIdList = this.roleMenuFunctionMapper.selectList(roleMenuWrapper)?.map { it.menuFunctionId!! }
                if (menuIdList != null) {
                    // 根据菜单ID集合查询菜单
                    val menuList = this.list(
                            KtQueryWrapper(SysMenuDO::class.java).`in`(SysMenuDO :: menuId, menuIdList.distinct())
                                    .orderByAsc(SysMenuDO :: seq)
                    )
                    // 查询菜单对应的功能
                    val functionIdList = mutableListOf<String>()
                    menuList.forEach { menu ->
                        menu.functionId?.let { functionIdList.add(it) }
                    }
                    var functionMap: Map<String?, String?>? = null
                    if (functionIdList.isNotEmpty()) {
                        // 查询功能并转为map
                        functionMap = this.functionService.list(
                                KtQueryWrapper(SysFunctionDO::class.java).`in`(SysFunctionDO :: functionId, functionIdList.distinct())
                        ).stream().collect(Collectors.toMap(SysFunctionDO :: functionId) {if (it.url == null) "" else it.url})
                        // 循环遍历设置
                    }
                    // 将菜单转为树形实体
                    val menuTreeList = menuList.map {
                        val menuVo = BeanMapUtils.createFromParent(it, SysMenuVO :: class.java)
                        if (functionMap != null && functionMap[it.functionId] != null) {
                            menuVo.url = functionMap[it.functionId]
                        }
                        return@map this.convertMenu2Tree(menuVo)
                    }

                    // 构造树形结构
                    return TreeUtils.buildList(menuTreeList, "0")
                }
            }
        }
        return null
    }

    /**
     * 查询菜单列表和菜单包含的功能
     */
    override fun listWithFunction(parameters: QueryParameter): List<SysMenuVO>? {
        val queryWrapper = MybatisUtil.createQueryWrapperFromParameters(parameters, SysMenuDO :: class.java)
        val menuListBase = this.list(queryWrapper.orderByAsc(MybatisUtil.getDbField(SysMenuDO :: seq)))
        val menuList = menuListBase.map {
            return@map BeanMapUtils.createFromParent(it, SysMenuVO :: class.java)
        }
        val functionIdList = mutableListOf<String>()
        // 获取菜单对应的功能
        menuList.forEach {
            if (it.functionId != null) {
                functionIdList.add(it.functionId!!)
            }
        }
        var functionList = listOf<SysFunctionDO>()
        if (functionIdList.size > 0) {
            // 查询本级
            val wrapper = KtQueryWrapper(SysFunctionDO :: class.java)
            if (functionIdList.size == 1) {
                wrapper.eq(SysFunctionDO :: functionId, functionIdList[0])
            } else {
                wrapper.`in`(SysFunctionDO :: functionId, functionIdList)
            }
            functionList = this.functionService.list(wrapper)
        }

        if (functionList.isNotEmpty()) {
            // 查询下级
            val childWrapper = KtQueryWrapper(SysFunctionDO :: class.java)
            childWrapper.`in`(SysFunctionDO :: parentId, functionIdList)
            val childFunctionList = this.functionService.list(childWrapper)
            if (childFunctionList != null) {
                // 将下级分组，并设置到function
                val childrenMap = childFunctionList.groupBy { it.parentId }
                functionList.forEach {
                    it.children = childrenMap[it.functionId]
                }
            }
            // 将功能列表转为Map
            val functionMap = functionList.map { it.functionId to it }.toMap()
            menuList.forEach {
                if (it.functionId != null) {
                    it.function = functionMap[it.functionId]
                }
            }
        }

        return menuList
    }

    /**
     * 保存下级菜单
     */
    override fun saveChildrenMenu(parameters: Map<String, Any?>): Boolean? {
        // 获取menuId和functionId
        val menuId = parameters.get("menuId") as String?
        val functionId = parameters.get("functionId") as String?
        val menuConfigId = parameters.get("menuConfigId") as String?
        if (!StringUtils.isEmpty(menuId) && !StringUtils.isEmpty(functionId) && !StringUtils.isEmpty(menuConfigId)) {
            // 查询菜单所属配置信息
            val functionTree = this.functionService.queryWithChildren(functionId!!)
            if (functionTree != null) {
                val menuList = mutableListOf<SysMenuDO>()
                this.converFunction2Menu(functionTree, menuId!!, menuConfigId!!, menuList)
                // 保存菜单信息
                this.saveBatch(menuList)
                return true
            }
        }
        return false
    }

    /**
     * 将菜单转为树对象
     */
    private fun convertMenu2Tree(menu: SysMenuVO): Tree<SysMenuVO> {
        val tree = Tree<SysMenuVO>()
        tree.id = menu.menuId
        tree.text = menu.menuName
        tree.parentId = menu.parentId
        tree.`object` = menu
        return tree
    }

    /**
     * 将功能树形结构转为菜单
     */
    private fun converFunction2Menu(functionTree: Tree<SysFunctionDO>, menuId: String, menuConfigId: String, menuList: MutableList<SysMenuDO>) {
        val function = functionTree.`object`!!
        if ("1" == function.functionType || "0" == function.functionType) {
            val id = UUIDGenerator.getUUID()
            val menu = SysMenuDO()
            menu.menuId = id
            menu.menuName = function.functionName
            menu.parentId = menuId
            menu.icon = function.icon
            menu.menuConfigId = menuConfigId
            menu.createTime = Date()
            menu.createUserId = AuthUtils.getCurrentUserId()
            menu.seq = function.seq
            if ("1" == function.functionType) {
                menu.functionId = function.functionId
            }
            menuList.add(menu)
            if ("0" == function.functionType && functionTree.children.size > 0) {
                functionTree.children.forEach {
                    this.converFunction2Menu(it, id, menuConfigId, menuList)
                }
            }

        }
    }

    /**
     * 重写更新保存方法
     */
    override fun saveOrUpdate(menu: SysMenuDO): Boolean {
        if (StringUtils.isEmpty(menu.parentId)) {
            menu.parentId = "0"
        }
        val getEntity = this.getById(menu.menuId)
        if (getEntity == null) {
            // 保存操作
            menu.createTime = Date()
            menu.createUserId = AuthUtils.getCurrentUserId()
        } else {
            menu.updateTime = Date()
            menu.updateUserId = AuthUtils.getCurrentUserId()
        }
        return super<BaseServiceImpl>.saveOrUpdate(menu)
    }
}