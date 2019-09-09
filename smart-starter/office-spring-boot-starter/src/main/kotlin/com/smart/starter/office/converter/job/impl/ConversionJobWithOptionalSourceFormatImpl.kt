package com.smart.starter.office.converter.job.impl

import com.artofsolving.jodconverter.DocumentFormat
import com.smart.starter.office.converter.ConversionProperties
import com.smart.starter.office.converter.job.ConversionJobWithOptionalSourceFormat
import com.smart.starter.office.converter.job.ConversionJobWithSource

class ConversionJobWithOptionalSourceFormatImpl(private val conversionProperties: ConversionProperties) : ConversionJobWithSourceImpl(conversionProperties), ConversionJobWithOptionalSourceFormat {

    override fun `as`(format: DocumentFormat): ConversionJobWithSource {
        this.conversionProperties.sourceFormat = format
        return this
    }
}