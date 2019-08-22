package com.smart.portal.service.impl

import com.smart.portal.mapper.NewsAttachmentMapper
import com.smart.portal.model.NewsAttachmentDO
import com.smart.portal.service.NewsAttachmentService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.stereotype.Service

@Service
class NewsAttachmentServiceImpl : BaseServiceImpl<NewsAttachmentMapper, NewsAttachmentDO>(), NewsAttachmentService {
}