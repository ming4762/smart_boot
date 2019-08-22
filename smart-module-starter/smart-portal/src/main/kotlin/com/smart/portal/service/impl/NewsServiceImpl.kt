package com.smart.portal.service.impl

import com.smart.auth.common.utils.AuthUtils
import com.smart.portal.mapper.NewsMapper
import com.smart.portal.model.NewsDO
import com.smart.portal.service.NewsService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.stereotype.Service
import java.util.*

@Service
class NewsServiceImpl : BaseServiceImpl<NewsMapper, NewsDO>(), NewsService {

    /**
     * 重写保存修改方法
     */
    override fun saveOrUpdate(entity: NewsDO): Boolean {
        var isAdd = false
        if (entity.newsId == null) {
            isAdd = true
        } else if (this.getById(entity.newsId) == null) {
            isAdd = true
        }
        if (isAdd) {
            entity.createTime = Date()
            entity.createUserId = AuthUtils.getCurrentUserId()
        } else {
            entity.updateTime = Date()
            entity.updateUserId = AuthUtils.getCurrentUserId()
        }
        return super.saveOrUpdate(entity)
    }
}