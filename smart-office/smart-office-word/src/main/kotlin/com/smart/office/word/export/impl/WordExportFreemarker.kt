package com.smart.office.word.export.impl

import com.smart.office.word.export.WordExport
import freemarker.template.Configuration
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * 使用freemarker导出word
 * @author ming
 * 2019/7/15 下午4:46
 */
class WordExportFreemarker() : WordExport {

    private var configuration = Configuration(Configuration.VERSION_2_3_28)

    init {
        configuration.defaultEncoding = "utf-8"
    }

    /**
     * 导出word
     */
    override fun export(templatePath: String, data: Map<String, Any>, outputStream: OutputStream) {
        configuration.setDirectoryForTemplateLoading(File("/"))
        val template = configuration.getTemplate(templatePath)
        val writer = OutputStreamWriter(outputStream, "utf-8")
        template.process(data, writer)
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val out = File("/Users/ming/Desktop/sdshj/ceshi/测试2.doc")
            val fos = FileOutputStream(out)
            WordExportFreemarker().export("/Users/ming/Desktop/sdshj/template.ftl", mapOf(
                    "year" to "2019",
                    "month" to "9",
                    "journal" to "26"
            ), fos)
        }
    }
}