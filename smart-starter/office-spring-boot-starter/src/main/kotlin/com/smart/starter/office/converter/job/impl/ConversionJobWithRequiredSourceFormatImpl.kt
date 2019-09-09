package com.smart.starter.office.converter.job.impl

import com.artofsolving.jodconverter.DocumentFormat
import com.smart.starter.office.converter.ConversionProperties
import com.smart.starter.office.converter.job.ConversionJobWithRequiredSourceFormat
import com.smart.starter.office.converter.job.ConversionJobWithSource

class ConversionJobWithRequiredSourceFormatImpl(private val conversionProperties: ConversionProperties) : ConversionJobWithRequiredSourceFormat {

    /**
     * 设置输入格式
     */
    override fun `as`(format: DocumentFormat): ConversionJobWithSource {
        this.conversionProperties.sourceFormat = format
        return ConversionJobWithSourceImpl(this.conversionProperties)
    }
}