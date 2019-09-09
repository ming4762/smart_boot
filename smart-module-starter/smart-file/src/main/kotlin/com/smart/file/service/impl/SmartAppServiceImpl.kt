package com.smart.file.service.impl

import com.smart.file.mapper.SmartAppMapper
import com.smart.file.model.SmartAppDO
import com.smart.file.service.SmartAppService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.stereotype.Service
import java.util.*

/**
 *
 * @author ming
 * 2019/9/7 下午4:34
 */
@Service
class SmartAppServiceImpl : BaseServiceImpl<SmartAppMapper, SmartAppDO>(), SmartAppService {

    override fun saveOrUpdate(entity: SmartAppDO): Boolean {
        var isAdd = false
        if (entity.appId == null) {
            isAdd = true
        } else if (this.getById(entity.appId) == null) {
            isAdd = true
        }
        if (isAdd) {
            entity.createTime = Date()
            return this.save(entity)
        } else {
            return this.updateById(entity)
        }
    }
}