package com.smart.file.controller

import com.smart.common.message.Result
import com.smart.file.model.SmartAppVersionDO
import com.smart.file.service.SmartAppVersionService
import com.smart.starter.crud.controller.BaseController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 *
 * @author ming
 * 2019/9/7 下午4:40
 */
@RestController
@RequestMapping("app/version")
class SmartAppVersionController : BaseController<SmartAppVersionService, SmartAppVersionDO>() {

    /**
     * 保存版本信息
     */
    @PostMapping("saveWithFile")
    fun saveWithFile(@RequestParam("files") files: List<MultipartFile>, version: SmartAppVersionDO): Result<Boolean?> {
        return try {
            if (files.isEmpty()) {
                Result.failure("保存失败，未找到文件信息")
            } else {
                Result.success(this.service.saveWithFile(files[0], version))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message)
        }
    }
}