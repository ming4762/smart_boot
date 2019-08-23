package com.smart.file.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * 图片接口
 * @author ming
 * 2019/8/22 上午8:24
 */
@Controller
@RequestMapping("/image")
class ImageController : PublicImageController() {
}