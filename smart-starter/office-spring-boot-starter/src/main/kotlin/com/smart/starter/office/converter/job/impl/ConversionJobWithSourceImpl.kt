package com.smart.starter.office.converter.job.impl

import com.smart.starter.office.converter.ConversionProperties
import com.smart.starter.office.converter.job.ConversionJobWithOptionalTargetFormat
import com.smart.starter.office.converter.job.ConversionJobWithRequiredTargetFormat
import com.smart.starter.office.converter.job.ConversionJobWithSource
import java.io.File
import java.io.OutputStream

open class ConversionJobWithSourceImpl(private val conversionProperties: ConversionProperties) : ConversionJobWithSource {

    /**
     * 设置输出流
     */
    override fun to(outputStream: OutputStream): ConversionJobWithRequiredTargetFormat {
        this.conversionProperties.targetOutputStream = outputStream
        return ConversionJobWithRequiredTargetFormatImpl(this.conversionProperties)
    }

    /**
     * 设置输出文件
     */
    override fun to(target: File): ConversionJobWithOptionalTargetFormat {
        this.conversionProperties.targetFile = target
        return ConversionJobWithOptionalTargetFormatImpl(this.conversionProperties)
    }
}