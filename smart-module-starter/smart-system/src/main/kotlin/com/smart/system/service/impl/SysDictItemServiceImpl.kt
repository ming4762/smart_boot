package com.smart.system.service.impl

import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.smart.auth.common.utils.AuthUtils
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.starter.crud.utils.MybatisUtil
import com.smart.system.mapper.SysDictItemMapper
import com.smart.system.model.SysDictItemDO
import com.smart.system.service.SysDictItemService
import org.springframework.stereotype.Service
import java.util.*

@Service
class SysDictItemServiceImpl : BaseServiceImpl<SysDictItemMapper, SysDictItemDO>(), SysDictItemService {

    override fun saveOrUpdate(entity: SysDictItemDO): Boolean {
        var isSave = false
        if (entity.id === null) {
            isSave = true
        } else if (this.getById(entity.id) == null) {
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

    override fun list(queryWrapper: Wrapper<SysDictItemDO>?): MutableList<SysDictItemDO> {
        if (queryWrapper != null) {
            queryWrapper as QueryWrapper
            queryWrapper.orderByAsc(MybatisUtil.getDbField(SysDictItemDO :: seq))
        }
        return super<BaseServiceImpl>.list(queryWrapper)
    }
}