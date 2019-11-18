package com.smart.office.word.export

import java.io.OutputStream

/**
 * word导出
 * @author ming
 * 2019/7/15 下午4:44
 */
interface WordExport {

    /**
     * 导出word
     */
    fun export(templatePath: String, data: Map<String, Any>, outputStream: OutputStream)
}