package com.smart.file.service

import com.smart.file.model.SmartAppVersionDO
import com.smart.starter.crud.service.BaseService
import org.springframework.web.multipart.MultipartFile

/**
 *
 * @author ming
 * 2019/9/7 下午4:39
 */
interface SmartAppVersionService : BaseService<SmartAppVersionDO> {
    abstract fun saveWithFile(file: MultipartFile, version: SmartAppVersionDO): Boolean
}