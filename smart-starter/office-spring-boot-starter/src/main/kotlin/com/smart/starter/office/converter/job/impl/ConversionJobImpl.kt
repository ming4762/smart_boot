package com.smart.starter.office.converter.job.impl

import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter
import com.smart.starter.office.converter.ConversionProperties
import com.smart.starter.office.converter.job.ConversionJob
import org.slf4j.LoggerFactory
import java.net.ConnectException

open class ConversionJobImpl(private val conversionProperties: ConversionProperties) : ConversionJob {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ConversionJobImpl:: class.java)
    }

    /**
     * 执行转换
     */
    override fun convert() {
        val connection = this.conversionProperties.connection
        try {
            connection.connect()
            val converter = StreamOpenOfficeDocumentConverter(connection)
            if (this.conversionProperties.sourceFile != null && this.conversionProperties.targetFile != null) {
                converter.convert(
                        this.conversionProperties.sourceFile,
                        this.conversionProperties.sourceFormat,
                        this.conversionProperties.targetFile,
                        this.conversionProperties.targetFormat
                )
            } else if (this.conversionProperties.sourceInputStream != null && this.conversionProperties.targetOutputStream != null) {
                converter.convert(
                        this.conversionProperties.sourceInputStream,
                        this.conversionProperties.sourceFormat,
                        this.conversionProperties.targetOutputStream,
                        this.conversionProperties.targetFormat
                )
            } else if (this.conversionProperties.sourceFile != null && this.conversionProperties.targetOutputStream != null) {
                converter.convert(
                        this.conversionProperties.sourceFile!!.inputStream(),
                        this.conversionProperties.sourceFormat,
                        this.conversionProperties.targetOutputStream,
                        this.conversionProperties.targetFormat
                )
            }
        } catch (e: ConnectException) {
            LOGGER.error("pdf转换异常，openoffice服务未启动！")
            throw e
        } catch (e: OpenOfficeException) {
            LOGGER.error("pdf转换器异常，读取转换文件失败")
            throw e
        } finally {
            connection.disconnect()
        }
    }
}