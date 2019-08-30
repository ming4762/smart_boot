package com.smart.portal.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.utils.BeanMapUtils
import com.smart.portal.mapper.NewsMapper
import com.smart.portal.model.NewsAttachmentDO
import com.smart.portal.model.NewsDO
import com.smart.portal.model.NewsDTO
import com.smart.portal.service.NewsAttachmentService
import com.smart.portal.service.NewsService
import com.smart.portal.service.PortalModuleService
import com.smart.starter.crud.constants.CRUDConstants
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.starter.crud.utils.MybatisUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

@Service
class NewsServiceImpl : BaseServiceImpl<NewsMapper, NewsDO>(), NewsService {

    @Autowired
    private lateinit var moduleService: PortalModuleService

    @Autowired
    private lateinit var newsAttachmentService: NewsAttachmentService

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


    override fun list(queryWrapper: QueryWrapper<NewsDO>, parameters: Map<String, Any?>, paging: Boolean): List<NewsDO> {
        // moduleCode参数处理
        val moduleCode = parameters["moduleCode"]
        if (!StringUtils.isEmpty(moduleCode)) {
            // 查询模块
            val insql = "select module_id from smart_portal_module where module_code = '$moduleCode'"
            queryWrapper.inSql(MybatisUtil.getDbField(NewsDO :: moduleId), insql)
        }
        val list = super<BaseServiceImpl>.list(queryWrapper, parameters, paging)
        if (list.isNotEmpty()) {
            // 判断是否带有内容，性能优化
            val withContent = parameters["withContent"]
            if (withContent is Boolean && withContent == false) {
                list.forEach {
                    it.content = null
                }
            }
            // 查询所有
            val withAll = parameters[CRUDConstants.WITH_ALL.name]
            if (withAll is Boolean && withAll) {
                return this.listWithAll(list)
            }
        }
        return list
    }

    /**
     * 重写批量删除方法
     * 删除附件
     */
    override fun batchDelete(tList: List<NewsDO>): Int {
        // 删除附件
        this.newsAttachmentService.deleteByNewsId(tList.map { it.newsId!! })
        return super.batchDelete(tList)
    }

    /**
     * 重写查询详情
     */
    override fun queryDetail(t: NewsDO): NewsDO? {
        // 查询新闻
        val news = this.get(t)
        if (news != null) {
            // 查询附件
            val newsAttachmentList = this.newsAttachmentService.list(
                    KtQueryWrapper(NewsAttachmentDO::class.java).eq(NewsAttachmentDO :: newsId, news.newsId)
            )
            val newsDTO = BeanMapUtils.createFromParent(news, NewsDTO :: class.java)
            newsDTO.attachmentFileIdList = newsAttachmentList.map { it.fileId!! }
            return newsDTO
        }
        return null
    }

    /**
     * 查询所有
     */
    private fun listWithAll(newsList: List<NewsDO>): List<NewsDO> {
        // 查询模块信息
        val moduleIdList = newsList.mapNotNull { it.moduleId }.distinct()
        val moduleMap = if (moduleIdList.isEmpty()) mapOf() else this.moduleService.listByIds(moduleIdList)
                .map { it.moduleId to it }
                .toMap()

        return newsList.map {
            val newsDTO = BeanMapUtils.createFromParent(it, NewsDTO :: class.java)
            newsDTO.module = moduleMap[it.moduleId]
            return@map newsDTO
        }
    }
}