package com.smart.starter.office.converter.job

import java.io.File
import java.io.OutputStream

interface ConversionJobWithSource {

    /**
     * 设置输出点
     */
    fun to(outputStream: OutputStream): ConversionJobWithRequiredTargetFormat

    fun to(target: File): ConversionJobWithOptionalTargetFormat
}