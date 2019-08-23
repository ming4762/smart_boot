package com.smart.portal.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.file.service.FileService
import com.smart.portal.mapper.NewsAttachmentMapper
import com.smart.portal.model.NewsAttachmentDO
import com.smart.portal.service.NewsAttachmentService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class NewsAttachmentServiceImpl : BaseServiceImpl<NewsAttachmentMapper, NewsAttachmentDO>(), NewsAttachmentService {

    @Autowired
    private lateinit var fileService: FileService

    override fun saveBatch(entityList: MutableCollection<NewsAttachmentDO>): Boolean {
        val currentData = Date()
        entityList.forEach {
            it.createTime = currentData
        }
        return super<BaseServiceImpl>.saveBatch(entityList)
    }

    /**
     * 通过新闻ID删除，同时删除文件
     */
    override fun deleteByNewsId(newsIdList: List<String>): Boolean {
        return if (newsIdList.isNotEmpty()) {
            val query = KtQueryWrapper(NewsAttachmentDO :: class.java)
                    .`in`(NewsAttachmentDO :: newsId, newsIdList)
            // 查询文件ID列表
            val attachmentList = this.list(query)
            this.fileService.batchDeleteFile(attachmentList.map { it.fileId!! })
            this.remove(query)
        } else {
            true
        }
    }
}