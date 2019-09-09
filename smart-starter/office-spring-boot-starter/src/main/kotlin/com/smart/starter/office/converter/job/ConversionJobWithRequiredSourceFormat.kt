package com.smart.starter.office.converter.job

import com.artofsolving.jodconverter.DocumentFormat

/**
 * 设置转换format接口
 */
interface ConversionJobWithRequiredSourceFormat {

    // 设置resource format
    fun `as`(format: DocumentFormat): ConversionJobWithSource
}