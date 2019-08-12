package com.smart.starter.file.service.impl

import com.smart.common.utils.security.MD5Utils
import com.smart.starter.file.service.ActualFileService
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.InputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 本地磁盘存储文件
 * @author ming
 * @param basePath 文件存储基础路径
 * TODO: 待测试
 * 2019/6/15 下午8:24
 */
class ActualFileServiceDiskImpl(val basePath: String) : ActualFileService {

    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault())
    }

    /**
     * 保存文件
     */
    override fun save(file: File, filename: String?): String {
        // 获取文件路径
        return this.save(file.inputStream(), filename)
    }

    /**
     * 保存文件
     */
    override fun save(inputStream: InputStream, filename: String?): String {
        // 获取文件路径
        val md5 = MD5Utils.md5(inputStream)
        val filePath = this.getFileAbsolutePath(md5)
        val uploadFile = File(filePath)
        if (!uploadFile.exists()) {
            uploadFile.createNewFile()
        }
        IOUtils.copy(inputStream, uploadFile.outputStream())
        return this.getFileId(md5)
    }

    /**
     * 删除文件
     */
    override fun delete(id: String): Boolean {
        val filePath = this.getFileAbsolutePathById(id)
        //判断文件是否存在
        val file = File(filePath)
        if (file.exists()) {
            return file.delete()
        }
        return false
    }

    /**
     * 获取文件ID
     */
    private fun getFileId(md5: String): String {
        val dateStr = FORMATTER.format(Instant.now())
        return "${dateStr}_$md5"
    }

    /**
     * 获取文件绝对路径
     */
    private fun getFileAbsolutePath(md5: String): String {
        val dateStr = FORMATTER.format(Instant.now())
        return "${this.basePath}${File.separator}$dateStr${File.separator}$md5"
    }

    /**
     * 通过ID获取文件路径
     */
    private fun getFileAbsolutePathById(id: String): String {
        val ids = id.split("_")
        val datePathStr = ids[0]
        val md5 = ids[1]
        return "${this.basePath}${File.separator}$datePathStr${File.separator}$md5"
    }
}