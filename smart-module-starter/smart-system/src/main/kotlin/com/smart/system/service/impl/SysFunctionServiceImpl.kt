package com.smart.system.service.impl

import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.model.Tree
import com.smart.common.utils.TreeUtils
import com.smart.common.utils.UUIDGenerator
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.starter.crud.utils.MybatisUtil
import com.smart.system.mapper.SysFunctionMapper
import com.smart.system.mapper.SysMenuMapper
import com.smart.system.model.SysFunctionDO
import com.smart.system.model.SysMenuConfigDO
import com.smart.system.model.SysMenuDO
import com.smart.system.service.SysFunctionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.util.*
import java.util.stream.Collectors


/**
 * 功能服务层
 * @author zhongming
 */
@Service
class SysFunctionServiceImpl : BaseServiceImpl<SysFunctionMapper, SysFunctionDO>(), SysFunctionService {


    @Autowired
    private lateinit var menuMapper: SysMenuMapper

    /**
     * 重写LIST方法添加排序
     * TODO:排序根据参数执行
     */
    override fun list(queryWrapper: Wrapper<SysFunctionDO>, parameters: Map<String, Any?>, paging: Boolean): List<SysFunctionDO> {
        queryWrapper as QueryWrapper<SysFunctionDO>
        queryWrapper.orderByAsc(MybatisUtil.getDbField(SysFunctionDO :: seq))
        return super<BaseServiceImpl>.list(queryWrapper)
    }

    /**
     * 查询功能和下级
     */
    override fun queryWithChildren(functionId: String): Tree<SysFunctionDO>? {
        // 查询自身
        val function = this.getById(functionId)
        if (function != null) {
            // 使用递归查询下级
            val result = this.function2Tree(arrayListOf(function))
            this.getAllChildren(result)
            return result[0]
        }
        return null
    }

    /**
     * 获取所有下级
     */
    private fun getAllChildren(treeList: List<Tree<SysFunctionDO>>) {
        treeList.forEach {
            val wrapper = QueryWrapper<SysFunctionDO>()
            wrapper.eq("parent_id", it.id)
            if (SysFunctionDO.BUTTON != it.`object`!!.functionType) {
                val children = this.list(wrapper)
                if (children.isNotEmpty()) {
                    val treeFunctionList = this.function2Tree(children)
                    it.children = this.function2Tree(children)
                    this.getAllChildren(treeFunctionList)
                }
            }
        }
    }


    /**
     * 使用递归获取所有下级
     */
    private fun getAllChildren(function: SysFunctionDO, functionList: MutableList<SysFunctionDO>) {
        functionList.add(function)
        if (function.functionType != SysFunctionDO.BUTTON) {
            // 查询功能下级
            val wrapper = QueryWrapper<SysFunctionDO>()
            wrapper.eq("parent_id", function.functionId)
            val functions = this.list(wrapper)
            if (!functions.isEmpty()) {
                functions.forEach {
                    this.getAllChildren(it, functionList)
                }
            }
        }
    }

    /**
     * 重写删除方法
     * 同时删除下级
     * 删除功能同时删除默认配置
     */
    override fun batchDelete(tList: List<SysFunctionDO>): Int {
        val functionList = mutableListOf<SysFunctionDO>()
        tList.forEach {
            this.getAllChildren(it, functionList)
        }
        // 获取删除的所有ID
        val idSet = mutableSetOf<String>()
        val menuIdSet = mutableSetOf<String>()
        functionList.forEach {
            idSet.add(it.functionId!!)
            if (it.functionType != SysFunctionDO.BUTTON) {
                menuIdSet.add(it.functionId!!)
            }
        }
        // 删除功能和功能对应的菜单信息
        return this.baseMapper.deleteBatchIds(idSet) + this.menuMapper.deleteBatchIds(menuIdSet)
    }

    /**
     * 功能列表转为树形列表
     */
    private fun function2Tree(functionList: List<SysFunctionDO>): MutableList<Tree<SysFunctionDO>> {
        return functionList.stream().map {
            val tree = Tree<SysFunctionDO>()
            tree.id = it.functionId
            tree.text = it.functionName
            tree.parentId = it.parentId
            tree.`object` = it
            return@map tree
        }.collect(Collectors.toList())
    }

    /**
     * 重写保存修改方法
     */
    @Transactional(rollbackFor = [Exception :: class])
    @Deprecated("已废弃")
    override fun saveOrUpdate(entity: SysFunctionDO): Boolean {
        // 判断是插入还是更新
        var isInsert = true
        if (!StringUtils.isEmpty(entity.functionId) && this.getById(entity.functionId) != null) {
            isInsert = false
        } else {
            entity.functionId = UUIDGenerator.getUUID()
        }

        // 同时更新默认的菜单配置
        if (entity.functionType != SysFunctionDO.BUTTON) {
            this.saveUpdateDefaultMenu(entity, isInsert)
        }
        if (isInsert) {
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
     * 重写保存修改方法
     */
    @Transactional(rollbackFor = [Exception :: class])
    override fun saveUpdate(entity: SysFunctionDO): SysFunctionDO {
        // 判断是插入还是更新
        var isInsert = true
        if (!StringUtils.isEmpty(entity.functionId) && this.getById(entity.functionId) != null) {
            isInsert = false
        } else {
            entity.functionId = UUIDGenerator.getUUID()
        }

        // 同时更新默认的菜单配置
        if (entity.functionType != "2") {
            this.saveUpdateDefaultMenu(entity, isInsert)
        }
        if (isInsert) {
            entity.createTime = Date()
            entity.createUserId = AuthUtils.getCurrentUserId()
            this.save(entity)
        } else {
            entity.updateTime = Date()
            entity.updateUserId = AuthUtils.getCurrentUserId()
            this.updateById(entity)
        }
        return entity
    }



    /**
     * 更新默认的菜单配置
     */
    private fun saveUpdateDefaultMenu(entity: SysFunctionDO, isInsert: Boolean) {
        val menu = this.convertFunction2Menu(entity, isInsert)
        if (isInsert) {
            this.menuMapper.insert(menu)
        } else {
            this.menuMapper.updateById(menu)
        }
    }

    /**
     * 功能转为菜单
     */
    private fun convertFunction2Menu(function: SysFunctionDO, isInsert: Boolean): SysMenuDO {
        val menu = SysMenuDO()
        menu.menuId = function.functionId
        menu.menuName = function.functionName
        menu.icon = function.icon
        menu.seq = function.seq
        menu.menuConfigId = SysMenuConfigDO.DEFAULT_CONFIG_ID
        menu.parentId = function.parentId

        if (isInsert) {
            menu.createTime = Date()
            menu.createUserId = AuthUtils.getCurrentUserId()
        } else {
            menu.updateTime = Date()
            menu.updateUserId = AuthUtils.getCurrentUserId()
        }

        if (function.functionType == "1") {
            menu.functionId = function.functionId
        }
        return menu
    }

    /**
     * 查询所有树形结构
     */
    override fun queryAllFunctionTree(parameters: Map<String, Any?>): List<Tree<SysFunctionDO>> {
        // 判断查询菜单功能还是非菜单功能
        var isMenu = parameters["isMenu"] as Boolean?
        if (isMenu == null) {
            isMenu = true
        }
        val wrapper = MybatisUtil.createQueryWrapperFromParameters(parameters, SysFunctionDO :: class.java)
        // 查询所有信息
        val list = this.list(wrapper)
        // 转为树形数据
        val treeList = list.stream().map {
            val tree = Tree<SysFunctionDO>()
            tree.id = it.functionId
            tree.text = it.functionName
            tree.`object` = it
            tree.parentId = it.parentId
            return@map tree
        }.collect(Collectors.toList())
        return TreeUtils.buildList(treeList, "0")
    }

    /**
     * 查询功能和下级
     */
    override fun queryWithChildren(parameters: Map<String, Any?>): List<Tree<SysFunctionDO>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}