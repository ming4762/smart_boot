package com.smart.system.service

import com.smart.common.model.Tree
import com.smart.starter.crud.service.BaseService
import com.smart.system.model.SysFunctionDO

interface SysFunctionService : BaseService<SysFunctionDO> {

    /**
     * 查询所有树形结构
     */
    fun queryAllFunctionTree(parameters: Map<String, Any?>): Tree<SysFunctionDO>?

    /**
     * 查询功能和下级
     */
    fun queryWithChildren(parameters: Map<String, Any?>): Tree<SysFunctionDO>?

    /**
     * 查询功能和下级
     */
    fun queryWithChildren(functionId: String): Tree<SysFunctionDO>?

    /**
     * 新增/更新
     */
    fun saveUpdate (entity: SysFunctionDO): SysFunctionDO
}