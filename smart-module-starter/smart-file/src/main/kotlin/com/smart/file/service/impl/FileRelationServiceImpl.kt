package com.smart.file.service.impl

import com.smart.file.mapper.FileRelationMapper
import com.smart.file.model.FileRelationDO
import com.smart.file.service.FileRelationService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.stereotype.Service

@Service
class FileRelationServiceImpl : BaseServiceImpl<FileRelationMapper, FileRelationDO>(), FileRelationService {
}