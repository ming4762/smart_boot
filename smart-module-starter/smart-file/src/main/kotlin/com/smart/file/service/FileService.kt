package com.smart.file.service

import com.smart.file.model.SmartFileDO
import com.smart.file.model.dto.SmartFileDTO
import com.smart.starter.crud.service.BaseService
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.io.OutputStream

interface FileService : BaseService<SmartFileDO> {
    fun saveFile(multipartFile: MultipartFile, cloudFile: SmartFileDO): SmartFileDO

    fun saveFile(smartFileDTO: SmartFileDTO): SmartFileDO

    fun saveFile(multipartFile: MultipartFile, type: String): SmartFileDO

    fun deleteFile (id: String): SmartFileDO?

    fun batchDeleteFile (fileIdList: List<String>): Boolean

    fun downLoad(id: String): SmartFileDTO?

    fun downLoad(file: SmartFileDO): SmartFileDTO

    fun showImage(id: String, outputStream: OutputStream, width: Int?, height: Int?)
    fun convertPDF(id: String, cache: Boolean): InputStream

    fun convertPDF(id: String, outputStream: OutputStream, cache: Boolean)
}