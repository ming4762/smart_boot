package com.smart.system.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.smart.common.log.annotation.Log
import com.smart.common.message.Result
import com.smart.common.model.Tree
import com.smart.starter.crud.constants.CRUDConstants
import com.smart.starter.crud.controller.BaseController
import com.smart.starter.crud.query.PageQueryParameter
import com.smart.starter.crud.utils.MybatisUtil
import com.smart.system.model.SysRoleDO
import com.smart.system.model.SysRoleMenuFunctionDO
import com.smart.system.service.SysRoleMenuFunctionService
import com.smart.system.service.SysRoleService
import com.smart.system.service.SysUserRoleService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/6/12 下午3:50
 */
@RestController
@RequestMapping("/sys/role")
class SysRoleController : BaseController<SysRoleService, SysRoleDO>() {

    @Autowired
    private lateinit var userRoleService: SysUserRoleService

    @Autowired
    private lateinit var roleMenuFunctionService: SysRoleMenuFunctionService

    /**
     * 查询所有
     */
    @RequiresPermissions("system:role:query")
    @RequestMapping("/listWithAll")
    fun listWithAll(@RequestBody parameter: PageQueryParameter): Result<Any?> {
        parameter[CRUDConstants.WITH_ALL.name] = true
        return this.list(parameter)
    }

    /**
     * 查询树形结构带有组织结构
     */
    @RequiresPermissions("system:role:query")
    @RequestMapping("/listAllTreeWithOrgan")
    fun listAllTreeWithOrgan(): Result<Tree<Any>?> {
        return try {
            Result.success(this.service.listAllTreeWithOrgan())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 查询角色详情
     */
    @RequiresPermissions("system:role:query")
    @RequestMapping("/queryDetail")
    fun queryDetail(@RequestBody roleDO: SysRoleDO): Result<SysRoleDO?> {
        return try {
            Result.success(this.service.queryDetail(roleDO))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 查询角色对应的ID
     */
    @RequestMapping("/getRoleUserId")
    @RequiresPermissions("system:role:query")
    fun getRoleUserId(@RequestBody role: SysRoleDO): Result<List<String>?> {
        try {
            if (StringUtils.isEmpty(role.roleId)) {
                return Result.success(arrayListOf())
            }
            val userRoleMap = this.userRoleService.queryByRoleIds(arrayListOf(role.roleId!!))
            val userRoleList = userRoleMap[role.roleId!!] ?: return Result.success(arrayListOf())
            val userIdList = mutableListOf<String>()
            userRoleList.forEach {
                if (it.roleId == role.roleId) {
                    userIdList.add(it.userId!!)
                }
            }
            return Result.success(userIdList)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message)
        }
    }

    /**
     * 更新角色对应的人员信息
     */
    @RequiresPermissions("system:role:updateUser")
    @RequestMapping("/updateUser")
    @Log("更新角色人员信息")
    fun updateUser(@RequestBody parameters: Map<String, List<String>>): Result<Boolean?> {
        try {
            return Result.success(this.service.updateUser(parameters))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message, false)
        }
    }

    /**
     * 获取角色授权信息
     */
    @RequiresPermissions("system:role:query")
    @PostMapping("/queryAuthentication")
    fun queryAuthentication(@RequestBody roleMenuFunction: SysRoleMenuFunctionDO): Result<List<SysRoleMenuFunctionDO>?> {
        try {
            val wrapper = QueryWrapper<SysRoleMenuFunctionDO>()
            wrapper.eq(MybatisUtil.getDbField(SysRoleMenuFunctionDO :: roleId), roleMenuFunction.roleId)
                    .eq(MybatisUtil.getDbField(SysRoleMenuFunctionDO :: menuConfigId), roleMenuFunction.menuConfigId)
            return Result.success(this.roleMenuFunctionService.list(wrapper))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message, null)
        }
    }

    /**
     * 角色授权
     */
    @RequiresPermissions("system:role:authorize")
    @PostMapping("/authorize")
    @Log("角色授权")
    fun authorize(@RequestBody list: List<SysRoleMenuFunctionDO>): Result<Boolean?> {
        try {
            return Result.success(this.service.authorize(list))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e.message, false)
        }
    }

}