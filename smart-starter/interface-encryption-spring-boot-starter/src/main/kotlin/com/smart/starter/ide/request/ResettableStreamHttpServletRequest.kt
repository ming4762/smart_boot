package com.smart.starter.ide.request

import org.apache.commons.io.IOUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/**
 * 解决request inputStream 只能读取一次、无法修改的问题
 * @author ming
 */
class ResettableStreamHttpServletRequest(private val request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    // 数据
    private var data: ByteArray = IOUtils.toByteArray(this.request.inputStream)

    private var reader: BufferedReader? = null


    /**
     * 获取输入流
     */
    override fun getInputStream(): ServletInputStream {
        return ResettableServletInputStream(this.data)
    }


    override fun getReader(): BufferedReader {
        if (reader == null) {
            reader = BufferedReader(InputStreamReader(inputStream, characterEncoding));
        }
        return this.reader!!
    }

    /**
     * 修改inputstream
     */
    fun updateInputStream(data: ByteArray) {
        this.data = data
    }


    private class ResettableServletInputStream(data: ByteArray) : ServletInputStream() {

        var inputStream = ByteArrayInputStream(data)

        override fun isReady(): Boolean {
            return true
        }

        override fun isFinished(): Boolean {
            return inputStream.available() == 0
        }

        override fun read(): Int {
            return inputStream.read()
        }

        override fun setReadListener(listener: ReadListener?) {
        }
    }
}