package com.smart.portal.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.utils.UUIDGenerator
import com.smart.portal.mapper.PortalModuleMapper
import com.smart.portal.model.PortalModuleDO
import com.smart.portal.service.PortalModuleService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.stereotype.Service
import java.util.*

/**
 *
 * @author ming
 * 2019/8/7 上午10:47
 */
@Service
class PortalModuleServiceImpl : BaseServiceImpl<PortalModuleMapper, PortalModuleDO>(), PortalModuleService {

    override fun saveOrUpdate(entity: PortalModuleDO): Boolean {
        var isAdd = false
        if (entity.moduleId === null) {
            isAdd = true
            entity.moduleId = UUIDGenerator.getUUID()
        } else if (this.getById(entity.moduleId) == null) {
            isAdd = true
        }
        if (isAdd) {
            entity.createTime = Date()
            entity.createUserId = AuthUtils.getCurrentUserId()
            if (entity.parentId == "0" || entity.parentId == null) {
                entity.topParentId = entity.moduleId
            } else {
                val parent = this.getById(entity.parentId)
                entity.topParentId = parent.topParentId
            }
        } else {
            entity.updateTime = Date()
            entity.updateUserId = AuthUtils.getCurrentUserId()
        }
        return super<BaseServiceImpl>.saveOrUpdate(entity)
    }

    /**
     * 重写删除方法，删除上级同时删除下级
     */
    override fun batchDelete(modelList: List<PortalModuleDO>): Int {
        val deleteIdList = mutableListOf<String>()
        modelList.forEach {
            deleteIdList.add(it.moduleId!!)
            this.getChildIdList(it.moduleId!!, deleteIdList)
        }
        deleteIdList.distinct()
        return if (deleteIdList.isEmpty()) 0 else this.baseMapper.deleteBatchIds(deleteIdList)
    }

    /**
     * 使用递归查询下级ID集合
     */
    private fun getChildIdList(parentId: String, idList: MutableList<String>) {
        // 查询下级
        val children = this.list(
                KtQueryWrapper(PortalModuleDO()).eq(PortalModuleDO :: parentId, parentId)
        )
        if (children.isNotEmpty()) {
            idList.addAll(children.map { it.moduleId!! })
            children.forEach {
                this.getChildIdList(it.moduleId!!, idList)
            }
        }
    }

    /**
     * 获取顶级ID集合
     */
    private fun getTopChildIdList(topIdList: List<String>): Map<String, List<String>> {
        return if (topIdList.isEmpty()) mapOf() else this.list(
                KtQueryWrapper(PortalModuleDO()).`in`(PortalModuleDO :: topParentId, topIdList)
        ).groupBy { it.topParentId!! }
                .map { entry -> entry.key to entry.value.map { it.moduleId!! } }
                .toMap()
    }
}