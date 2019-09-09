package com.smart.file.service.impl

import com.smart.file.constant.FileTypeConstants
import com.smart.file.mapper.SmartAppVersionMapper
import com.smart.file.model.SmartAppVersionDO
import com.smart.file.model.SmartFileDO
import com.smart.file.service.FileService
import com.smart.file.service.SmartAppVersionService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 *
 * @author ming
 * 2019/9/7 下午4:40
 */
@Service
class SmartAppVersionServiceImpl : BaseServiceImpl<SmartAppVersionMapper, SmartAppVersionDO>(), SmartAppVersionService {

    @Autowired
    private lateinit var fileService: FileService

    @Transactional
    override fun saveOrUpdate(entity: SmartAppVersionDO): Boolean {
        var isAdd = false
        if (entity.versionId == null) {
            isAdd = true
        } else if (this.getById(entity.versionId) == null) {
            isAdd = true
        }
        if (isAdd) {
            entity.createTime = Date()
            return this.save(entity)
        } else {
            return this.updateById(entity)
        }
    }

    /**
     * 保存文件信息
     */
    @Transactional
    override fun saveWithFile(file: MultipartFile, version: SmartAppVersionDO): Boolean {
        // 保存文件信息
        var smartFile = SmartFileDO()
        smartFile.type = FileTypeConstants.APP.value
        // 保存文件信息
        smartFile = this.fileService.saveFile(file, smartFile)
        try {
            version.fileId = smartFile.fileId
            return this.saveOrUpdate(version)
        } catch (e: Exception) {
            this.fileService.deleteFile(smartFile.fileId!!)
            throw e
        }
    }
}