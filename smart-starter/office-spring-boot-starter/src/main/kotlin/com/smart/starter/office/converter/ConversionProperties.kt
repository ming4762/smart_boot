package com.smart.starter.office.converter

import com.artofsolving.jodconverter.DocumentFormat
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * 转换参数
 */
class ConversionProperties(var connection: OpenOfficeConnection) {

    // 来源文件
    var sourceFile: File? = null

    // 输入流
    var sourceInputStream: InputStream? = null

    // 输入文件
    var targetFile: File? = null

    // 输出流
    var targetOutputStream: OutputStream? = null

    var sourceFormat: DocumentFormat? = null

    var targetFormat: DocumentFormat? = null
}