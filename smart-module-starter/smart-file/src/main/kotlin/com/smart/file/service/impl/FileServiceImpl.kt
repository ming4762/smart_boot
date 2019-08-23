package com.smart.file.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.common.utils.ImageUtil
import com.smart.file.constant.FileTypeConstants
import com.smart.file.mapper.FileMapper
import com.smart.file.model.SmartFileDO
import com.smart.file.model.dto.SmartFileDTO
import com.smart.file.service.FileService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.starter.file.service.ActualFileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.FileCopyUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.OutputStream
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author ming
 * 2019/8/21 下午5:32
 */
@Service
class FileServiceImpl : BaseServiceImpl<FileMapper, SmartFileDO>(),  FileService {

    /**
     * 文件执行器
     */
    @Autowired
    private lateinit var actualFileService: ActualFileService

    override fun saveFile(multipartFile: MultipartFile, cloudFile: SmartFileDO): SmartFileDO {
        // 创建文件传输对象
        val smartFileDTO = SmartFileDTO(multipartFile, cloudFile)
        return this.saveFile(smartFileDTO)
    }

    /**
     * 保存文件
     */
    @Transactional
    override fun saveFile(smartFileDTO: SmartFileDTO): SmartFileDO {
        // 根据MD5 判断文件是否已经存在
        val fileDOList = this.list(
                KtQueryWrapper(SmartFileDO::class.java).eq(SmartFileDO :: md5, smartFileDTO.smartFile.md5)
        )
        if (fileDOList.isEmpty()) {
            // 保存文件
            smartFileDTO.smartFile.dbId = this.saveActualFile(smartFileDTO)
            // 将文件信息保存到数据库
            if (StringUtils.isEmpty(smartFileDTO.smartFile.type)) {
                smartFileDTO.smartFile.type = FileTypeConstants.TEMP.value
            }
            try {
                this.save(smartFileDTO.smartFile)
            } catch (e: Exception) {
                this.actualFileService.delete(smartFileDTO.smartFile.dbId!!)
                throw e
            }
            return smartFileDTO.smartFile
        } else {
            return fileDOList.first()
        }
    }

    override fun deleteFile(id: String): SmartFileDO? {
        // 获取文件信息
        val fileDO = this.getById(id)
        if (fileDO != null) {
            // 删除文件
            fileDO.dbId?.let {
                this.actualFileService.delete(it)
            }
            this.removeById(id)
        }
        return fileDO
    }

    override fun downLoad(id: String): SmartFileDTO? {
        // 获取文件信息
        val fileDO = this.getById(id)
        if (fileDO != null) {
            return this.downLoad(fileDO)
        }
        return null
    }

    override fun downLoad(file: SmartFileDO): SmartFileDTO {
        return SmartFileDTO(file, this.actualFileService.download(file.dbId!!))
    }

    override fun showImage(id: String, outputStream: OutputStream, width: Int?, height: Int?) {
        // 下载文件
        val smartFileDTO = this.downLoad(id)
        if (smartFileDTO != null) {
            if (width == null) {
                FileCopyUtils.copy(smartFileDTO.inputStream, outputStream)
            } else if (height == null) {
                ImageUtil.compress(smartFileDTO.inputStream, outputStream, width)
            } else {
                ImageUtil.compress(smartFileDTO.inputStream, outputStream, width, height)
            }
        }
    }

    override fun showPDF(id: String, response: HttpServletResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 保存真实文件
     */
    private fun saveActualFile(fileDTO: SmartFileDTO): String {
        return this.actualFileService.save(fileDTO.inputStream, fileDTO.smartFile.fileName)
    }
}