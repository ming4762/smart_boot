package com.smart.starter.file.service.impl

import com.smart.starter.file.service.ActualFileService
import java.io.File
import java.io.InputStream

/**
 * 本地磁盘存储文件
 * @author ming
 * 2019/6/15 下午8:24
 */
class ActualFileServiceDiskImpl : ActualFileService {

    private val filePath = "/Users/ming/Documents/temp/fileServer"

    /**
     * 保存文件
     */
    override fun save(file: File, filename: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 保存文件
     */
    override fun save(inputStream: InputStream, filename: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 删除文件
     */
    override fun delete(id: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}