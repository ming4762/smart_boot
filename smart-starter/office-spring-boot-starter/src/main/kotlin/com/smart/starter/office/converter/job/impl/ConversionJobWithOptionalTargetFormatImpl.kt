package com.smart.starter.office.converter.job.impl

import com.artofsolving.jodconverter.DocumentFormat
import com.smart.starter.office.converter.ConversionProperties
import com.smart.starter.office.converter.job.ConversionJob
import com.smart.starter.office.converter.job.ConversionJobWithOptionalTargetFormat

class ConversionJobWithOptionalTargetFormatImpl(private val conversionProperties: ConversionProperties) : ConversionJobImpl(conversionProperties), ConversionJobWithOptionalTargetFormat {

    override fun `as`(format: DocumentFormat): ConversionJob {
        this.conversionProperties.targetFormat = format
        return this
    }
}