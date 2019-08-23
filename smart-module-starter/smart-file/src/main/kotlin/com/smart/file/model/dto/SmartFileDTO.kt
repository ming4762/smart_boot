package com.smart.file.model.dto

import com.smart.auth.common.utils.AuthUtils
import com.smart.common.utils.UUIDGenerator
import com.smart.common.utils.security.MD5Utils
import com.smart.file.model.SmartFileDO
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.*

class SmartFileDTO {

    var smartFile: SmartFileDO

    var inputStream: InputStream

    var withThumb: Boolean = true


    constructor(multipartFile: MultipartFile, cloudFile: SmartFileDO) {
        cloudFile.fileId = UUIDGenerator.getUUID()
        cloudFile.contentType = multipartFile.contentType
        cloudFile.createTime = Date()
        cloudFile.fileName = cloudFile.fileName?: multipartFile.originalFilename
        cloudFile.size = multipartFile.size
        cloudFile.md5 = MD5Utils.md5(multipartFile.inputStream)
        cloudFile.createUserId = AuthUtils.getCurrentUserId()
        this.smartFile = cloudFile
        this.inputStream = multipartFile.inputStream
    }

    constructor(cloudFile: SmartFileDO, inputStream: InputStream) {
        this.smartFile = cloudFile
        this.inputStream = inputStream
    }
}