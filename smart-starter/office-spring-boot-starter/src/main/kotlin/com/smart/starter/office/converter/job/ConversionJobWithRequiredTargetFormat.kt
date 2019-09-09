package com.smart.starter.office.converter.job

import com.artofsolving.jodconverter.DocumentFormat

/**
 * 目标进行格式化
 */
interface ConversionJobWithRequiredTargetFormat {

    fun `as`(format: DocumentFormat): ConversionJob
}