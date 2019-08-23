package com.smart.file.controller

import com.smart.file.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author ming
 * 2019/8/22 上午9:18
 */
@Controller
@RequestMapping("public/image")
class PublicImageController {

    @Autowired
    protected lateinit var fileService: FileService

    /**
     * 图片显示接口
     * @param id 文件ID
     * @param width 图片压缩宽度
     * @param height 图片压缩高度
     * @param thumbnail TODO:缩略图
     * @param response
     */
    @GetMapping("show/{id}")
    fun show(@PathVariable("id") id: String,
             @RequestParam(value = "width", required = false) width: Int?,
             @RequestParam(value = "height", required = false) height: Int?,
             @RequestParam(value = "thumbnail", required = false) thumbnail: Boolean = false,
             response: HttpServletResponse) {
        try {
            //调用服务层接口显示图片
            this.fileService.showImage(id, response.outputStream, width, height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}