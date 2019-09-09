package com.smart.starter.office

import com.smart.starter.office.constants.DocumentConverterConstants
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "smart.office")
class OfficeProperties {

    // pdf转换器
    var pdfConverter: String = DocumentConverterConstants.OPEN_OFFICE.value

    /**
     * openOffice配置
     */
    var openOffice = OpenOffice()

    /**
     * openOffice配置
     */
    class OpenOffice {
        // 服务IP地址
        var host: String? = null

        // 运行环境 1：windows 2:linux
        var environment = 1

        // 服务端口号
        var port = 8100
    }
}