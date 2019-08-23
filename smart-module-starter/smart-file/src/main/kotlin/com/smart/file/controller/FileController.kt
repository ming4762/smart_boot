package com.smart.file.controller

import com.smart.common.message.Result
import com.smart.file.model.SmartFileDO
import org.springframework.web.bind.annotation.*

/**
 *
 * @author ming
 * 2019/8/21 下午10:50
 */
@RestController
@RequestMapping("file")
class FileController : PublicFileController() {

    /**
     * 文件删除接口
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    fun delete(@PathVariable("id") id: String): Result<SmartFileDO?> {
        return try {
            Result.success(this.service.deleteFile(id))
        } catch (e: Exception) {
            Result.failure(e.message)
        }
    }
}