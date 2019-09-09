package com.smart.starter.office.converter

import com.smart.starter.office.converter.job.ConversionJobWithOptionalSourceFormat
import com.smart.starter.office.converter.job.ConversionJobWithRequiredSourceFormat
import java.io.File
import java.io.InputStream

/**
 * pdf转换器
 * @author zhongming
 */
interface DocumentConverter {
    fun from(source: File): ConversionJobWithOptionalSourceFormat

    fun from(source: InputStream): ConversionJobWithRequiredSourceFormat
}