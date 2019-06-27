package com.smart.system.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.model.Tree
import com.smart.common.utils.TreeUtils
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysOrganMapper
import com.smart.system.model.SysOrganDO
import com.smart.system.service.SysOrganService
import org.springframework.stereotype.Service
import java.util.*

/**
 *
 * @author ming
 * 2019/6/26 下午4:01
 */
@Service
class SysOrganServiceImpl : BaseServiceImpl<SysOrganMapper, SysOrganDO>(), SysOrganService {
    /**
     * 重写添加修改方法
     */
    override fun saveOrUpdate(entity: SysOrganDO): Boolean {
        var isAdd = false
        if (entity.organId == null) {
            isAdd = true
        } else if (this.getById(entity.organId) == null) {
            isAdd = true
        }
        return if (isAdd) {
            entity.createUserId = AuthUtils.getCurrentUserId()
            entity.createTime = Date()
            this.save(entity)
        } else {
            entity.updateTime = Date()
            entity.updateUserId = AuthUtils.getCurrentUserId()
            this.updateById(entity)
        }
    }

    /**
     * 查询树形结构
     */
    override fun listTree(topParentIdList: List<String>): Map<String, List<Tree<SysOrganDO>>?> {
        val treeMap = if (topParentIdList.isEmpty()) {
            mapOf("0" to this.list())
        } else {
            this.list(KtQueryWrapper(SysOrganDO::class.java).`in`(SysOrganDO :: topParentId, topParentIdList))
                    .groupBy { it.topParentId!! }
        }
        return treeMap.map { it.key to TreeUtils.buildList(it.value.map { organ ->
            val tree = Tree<SysOrganDO>()
            tree.id = organ.organId
            tree.text = organ.organName
            tree.`object` = organ
            tree.parentId = organ.parentId
            return@map tree
        }, "0") }.toMap()
    }
}