package com.smart.portal.service.impl

import com.smart.portal.mapper.NewsAttachmentMapper
import com.smart.portal.model.NewsAttachmentDO
import com.smart.portal.service.NewsAttachmentService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.stereotype.Service
import java.util.*

@Service
class NewsAttachmentServiceImpl : BaseServiceImpl<NewsAttachmentMapper, NewsAttachmentDO>(), NewsAttachmentService {

    override fun saveBatch(entityList: MutableCollection<NewsAttachmentDO>): Boolean {
        val currentData = Date()
        entityList.forEach {
            it.createTime = currentData
        }
        return super<BaseServiceImpl>.saveBatch(entityList)
    }
}