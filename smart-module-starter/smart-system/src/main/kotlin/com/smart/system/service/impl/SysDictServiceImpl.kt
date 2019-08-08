package com.smart.system.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.utils.AuthUtils
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysDictMapper
import com.smart.system.model.SysDictDO
import com.smart.system.model.SysDictItemDO
import com.smart.system.service.SysDictItemService
import com.smart.system.service.SysDictService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class SysDictServiceImpl : BaseServiceImpl<SysDictMapper, SysDictDO>(), SysDictService {

    @Autowired
    private lateinit var dictItemService: SysDictItemService

    /**
     * 重写更新保存方法
     */
    override fun saveOrUpdate(entity: SysDictDO): Boolean {
        var isSave = false
        if (entity.dictCode == null) {
            isSave = true
        } else if (this.getById(entity.dictCode) == null) {
                isSave = true
        }
        if (isSave) {
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
     * 重写批量删除方法
     */
    override fun batchDelete(tList: List<SysDictDO>): Int {
        // 删除字典项
        val dictCodeList = tList.map { it.dictCode!! }
        val deleteQuery = KtQueryWrapper(SysDictItemDO :: class.java)
                .`in`(SysDictItemDO :: dictCode, dictCodeList)
        this.dictItemService.remove(deleteQuery)
        // 删除字典
        return super.batchDelete(tList)
    }
}