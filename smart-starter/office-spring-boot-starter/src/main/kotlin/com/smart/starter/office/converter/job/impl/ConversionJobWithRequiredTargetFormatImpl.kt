package com.smart.starter.office.converter.job.impl

import com.artofsolving.jodconverter.DocumentFormat
import com.smart.starter.office.converter.ConversionProperties
import com.smart.starter.office.converter.job.ConversionJob
import com.smart.starter.office.converter.job.ConversionJobWithRequiredTargetFormat

class ConversionJobWithRequiredTargetFormatImpl(private val conversionProperties: ConversionProperties) : ConversionJobWithRequiredTargetFormat {

    /**
     * 设置输出格式
     */
    override fun `as`(format: DocumentFormat): ConversionJob {
        this.conversionProperties.targetFormat = format
        return ConversionJobImpl(this.conversionProperties)
    }
}