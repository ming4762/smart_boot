package com.smart.starter.office.converter.impl

import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection
import com.smart.starter.office.OfficeProperties
import com.smart.starter.office.converter.ConversionProperties
import com.smart.starter.office.converter.DocumentConverter
import com.smart.starter.office.converter.job.ConversionJobWithOptionalSourceFormat
import com.smart.starter.office.converter.job.ConversionJobWithRequiredSourceFormat
import com.smart.starter.office.converter.job.impl.ConversionJobWithOptionalSourceFormatImpl
import com.smart.starter.office.converter.job.impl.ConversionJobWithRequiredSourceFormatImpl
import java.io.File
import java.io.InputStream

class OpenOfficeDocumentConverter(var config: OfficeProperties.OpenOffice): DocumentConverter {

    // 转换器连接
    private var connection: OpenOfficeConnection = SocketOpenOfficeConnection(this.config.host, this.config.port)


    /**
     * 设置源
     */
    override fun from(source: InputStream): ConversionJobWithRequiredSourceFormat {
        val conversionProperties = ConversionProperties(this.connection)
        conversionProperties.sourceInputStream = source
        return ConversionJobWithRequiredSourceFormatImpl(conversionProperties)
    }

    /**
     * 设置源
     */
    override fun from(source: File): ConversionJobWithOptionalSourceFormat {
        val conversionProperties = ConversionProperties(this.connection)
        conversionProperties.sourceFile = source
        return ConversionJobWithOptionalSourceFormatImpl(conversionProperties)
    }

}