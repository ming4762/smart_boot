package com.smart.starter.file.service

import java.io.File
import java.io.InputStream

/**
 * 实际文件操作接口
 * @author ming
 * 2019/6/15 下午7:15
 */
interface ActualFileService {

    fun save(file: File, filename: String?): String

    fun save(inputStream: InputStream, filename: String?): String

    fun delete(id: String): Boolean

}