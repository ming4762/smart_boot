package com.smart.file.controller

import com.smart.common.message.Result
import com.smart.file.model.SmartFileDO
import com.smart.file.service.FileService
import com.smart.starter.crud.controller.BaseControllerQuery
import org.apache.commons.beanutils.BeanUtils
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author ming
 * 2019/8/22 上午9:06
 */
@RestController
@RequestMapping("public/file")
class PublicFileController : BaseControllerQuery<FileService, SmartFileDO>() {

    /**
     * 上传文件
     * @author zhongming
     */
    @PostMapping("upload")
    fun upload(@RequestParam("file") multipartFile: MultipartFile, file: SmartFileDO): Result<SmartFileDO?> {
        return try {
            Result.success(this.service.saveFile(multipartFile, file))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 批量上传文件接口
     */
    @PostMapping("/batchUpload")
    fun batchUpload(@RequestParam("files") files: List<MultipartFile>, cloudFile: SmartFileDO): Result<List<SmartFileDO>?> {
        return try {
            val smartFileList = mutableListOf<SmartFileDO>()
            files.forEach {
                val file = this.service.saveFile(it, BeanUtils.cloneBean(cloudFile) as SmartFileDO)
                smartFileList.add(file)
            }
            Result.success(smartFileList)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }

    /**
     * 文件下载接口
     */
    @GetMapping("download/{id}")
    @ResponseBody
    fun download (@PathVariable("id")id: String, response: HttpServletResponse) {
        try {
            val smartFileDTO = this.service.downLoad(id)
            if (smartFileDTO != null) {
                //设置文件名并转码
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(smartFileDTO.smartFile.fileName, "UTF-8"))
                //设置文件类型
                response.contentType = smartFileDTO.smartFile.contentType
                //写入文件流
                FileCopyUtils.copy(smartFileDTO.inputStream, response.outputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}