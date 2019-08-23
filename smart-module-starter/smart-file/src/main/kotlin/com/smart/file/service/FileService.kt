package com.smart.file.service

import com.smart.file.model.SmartFileDO
import com.smart.file.model.dto.SmartFileDTO
import com.smart.starter.crud.service.BaseService
import org.springframework.web.multipart.MultipartFile
import java.io.OutputStream
import javax.servlet.http.HttpServletResponse

interface FileService : BaseService<SmartFileDO> {
    fun saveFile(multipartFile: MultipartFile, cloudFile: SmartFileDO): SmartFileDO

    fun saveFile(smartFileDTO: SmartFileDTO): SmartFileDO

    fun deleteFile (id: String): SmartFileDO?

    fun downLoad(id: String): SmartFileDTO?

    fun downLoad(file: SmartFileDO): SmartFileDTO

    fun showImage(id: String, outputStream: OutputStream, width: Int?, height: Int?)
    fun showPDF(id: String, response: HttpServletResponse)
}