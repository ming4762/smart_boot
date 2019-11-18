package com.smart.file.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.common.spring.ApplicationContextRegister
import com.smart.common.utils.FileUtils
import com.smart.common.utils.ImageUtil
import com.smart.common.utils.UUIDGenerator
import com.smart.common.utils.security.MD5Utils
import com.smart.file.constant.FileRelationTypeConstants
import com.smart.file.constant.FileTypeConstants
import com.smart.file.mapper.FileMapper
import com.smart.file.model.FileRelationDO
import com.smart.file.model.SmartFileDO
import com.smart.file.model.dto.SmartFileDTO
import com.smart.file.service.FileRelationService
import com.smart.file.service.FileService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.starter.file.service.ActualFileService
import com.smart.starter.office.converter.DefaultDocumentFormatRegistry
import com.smart.starter.office.converter.DocumentConverter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.FileCopyUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 *
 * @author ming
 * 2019/8/21 下午5:32
 */
@Service
class FileServiceImpl : BaseServiceImpl<FileMapper, SmartFileDO>(),  FileService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(FileServiceImpl :: class.java)
    }

    /**
     * 文件执行器
     */
    @Autowired
    private lateinit var actualFileService: ActualFileService

    @Autowired
    private lateinit var fileRelationService: FileRelationService

    override fun saveFile(multipartFile: MultipartFile, cloudFile: SmartFileDO): SmartFileDO {
        // 创建文件传输对象
        val smartFileDTO = SmartFileDTO(multipartFile, cloudFile)
        return this.saveFile(smartFileDTO)
    }

    /**
     * 保存文件
     */
    override fun saveFile(multipartFile: MultipartFile, type: String): SmartFileDO {
        val smartFileDO = SmartFileDO()
        smartFileDO.type = type
        return this.saveFile(multipartFile, smartFileDO)
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

    /**
     * 批量删除文件
     */
    override fun batchDeleteFile(fileIdList: List<String>): Boolean {
        return if (fileIdList.isNotEmpty()) {
            val fileList = this.listByIds(fileIdList)
            fileList.forEach { file ->
                file.dbId?.let {
                    this.actualFileService.delete(it)
                }
            }
            this.removeByIds(fileIdList)
        } else {
            true
        }
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

    /**
     * 进行pdf转换
     * @param id 文件ID
     * @param cache 是否缓存
     */
    @Transactional
    override fun convertPDF(id: String, cache: Boolean): InputStream {
        val sourceFile = this.getById(id) ?: throw java.lang.Exception("转换PDF失败，未找到原文件")
        // 判断文件本身是否是PDF
        val fileExtension = FileUtils.getExtension(sourceFile.fileName!!)
        if (fileExtension == "PDF" || fileExtension == "pdf") {
            LOGGER.debug("文件是pdf格式无需转换")
            return this.downLoad(sourceFile).inputStream
        }
        var pdfId: String? = null
        // 获取是否已经存在pdf
        if (cache) {
            // 查询是否已经存在pdf文件
            val pdfList = this.fileRelationService.list(
                    KtQueryWrapper(FileRelationDO()).eq(FileRelationDO :: fileId, id)
                            .eq(FileRelationDO :: relationType, FileRelationTypeConstants.CONVERT_PDF.name)
            )
            if (pdfList.isNotEmpty()) {
                pdfId = pdfList[0].relationFileId
            } else {
                // 判断MD5相同的文件是否存在pdf转换
                val md5Files = this.list(
                        KtQueryWrapper(SmartFileDO()).eq(SmartFileDO :: md5, sourceFile.md5)
                                .ne(SmartFileDO :: fileId, sourceFile.fileId)
                )
                // 查询MD5相同文件的PDF转换
                if (md5Files.isNotEmpty()) {
                    // 查找MD5相同的文件是否已经存在pdf转换
                    val fileRelationList = this.fileRelationService.list(
                            KtQueryWrapper(FileRelationDO()).`in`(FileRelationDO :: fileId, md5Files.map { it.fileId!! })
                                    .eq(FileRelationDO :: relationType, FileRelationTypeConstants.CONVERT_PDF.name)
                    )
                    if (fileRelationList.isNotEmpty()) pdfId = fileRelationList[0].relationFileId
                }
            }
        }
        if (pdfId != null) {
            val file = this.downLoad(pdfId)
            if (file != null) {
                // 写入文件
                return file.inputStream
            }
        }
        // 转换PDF文件
        val file = this.downLoad(sourceFile)
        // 获取文件扩展名
        var extension = FileUtils.getExtension(file.smartFile.fileName!!)
        if (extension == "docx") extension = "doc"
        if (extension == "xlsx") extension = "xls"
        // 执行转换
        val pdfOutputStream = ByteArrayOutputStream()
        val documentConverter = ApplicationContextRegister.getBean(DocumentConverter :: class.java) ?: throw Exception("PDF转换失败，请初始化转换器（在启动类他添加@EnableDocumentConvert注解）")
        documentConverter.from(file.inputStream)
                .`as`(DefaultDocumentFormatRegistry.getFormatByFileExtension(extension))
                .to(pdfOutputStream)
                .`as`(DefaultDocumentFormatRegistry.PDF)
                .convert()
        // 判断是否要缓存
        val byteArray = pdfOutputStream.toByteArray()
        if (cache) {
            val inputStream = ByteArrayInputStream(byteArray)
            // 保存文件信息
            file.inputStream = inputStream
            file.smartFile.createTime = Date()
            file.smartFile.type = "${file.smartFile.type}-${FileRelationTypeConstants.CONVERT_PDF.name}"
            file.smartFile.md5 = MD5Utils.md5(ByteArrayInputStream(byteArray))
            file.smartFile.fileId = UUIDGenerator.getUUID()
            this.saveFile(file)
            // 保存文件关系信息
            val fileRelationDO = FileRelationDO()
            fileRelationDO.fileId = id
            fileRelationDO.relationFileId = file.smartFile.fileId
            fileRelationDO.relationType = FileRelationTypeConstants.CONVERT_PDF.name
            this.fileRelationService.save(fileRelationDO)
        }
        // 返回流
        return ByteArrayInputStream(byteArray)
    }

    /**
     * PDF转换
     * @param id 需要转换的文件ID
     * @param outputStream 输出流
     * @param cache 是否要缓存
     */
    override fun convertPDF(id: String, outputStream: OutputStream, cache: Boolean) {
        val inputStream = this.convertPDF(id, cache)
        FileCopyUtils.copy(inputStream, outputStream)
    }

    /**
     * 保存真实文件
     */
    private fun saveActualFile(fileDTO: SmartFileDTO): String {
        return this.actualFileService.save(fileDTO.inputStream, fileDTO.smartFile.fileName!!)
    }
}