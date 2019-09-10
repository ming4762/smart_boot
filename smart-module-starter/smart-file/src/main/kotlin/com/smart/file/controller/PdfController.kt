package com.smart.file.controller

import com.smart.common.message.Result
import com.smart.file.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author ming
 * 2019/9/10 上午9:07
 */
@RestController
@RequestMapping("public/pdf")
class PdfController {

    @Autowired
    private lateinit var fileService: FileService

    /**
     * 转换并查看PDF
     * @author zhongming
     */
    @GetMapping("/convertShowPDF/{id}")
    fun convertShowPDF(@PathVariable("id")id: String, response: HttpServletResponse) {
        this.fileService.convertPDF(id, response.outputStream, true)
    }

    /**
     * PDF查看
     * @author zhongming
     */
    @GetMapping("/showPDF/{id}")
    fun showPDF(@PathVariable("id")id: String, response: HttpServletResponse) {
        val file = this.fileService.downLoad(id) ?: throw Exception("未找到PDF文件")
        FileCopyUtils.copy(file.inputStream, response.outputStream)
    }

    /**
     * 转换pdf
     * @author zhongming
     */
    @PostMapping("/convert/{id}")
    fun convert(@PathVariable("id")id: String): Result<Boolean?> {
        this.fileService.convertPDF(id, true)
        return Result.success(true)
    }
}