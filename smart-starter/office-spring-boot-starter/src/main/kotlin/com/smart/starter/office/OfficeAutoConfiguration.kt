package com.smart.starter.office

import com.smart.starter.office.constants.DocumentConverterConstants
import com.smart.starter.office.converter.DocumentConverter
import com.smart.starter.office.converter.impl.OpenOfficeDocumentConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * 自动配置类
 * @author ming
 * 2019/9/9 下午2:15
 */
@Configuration
@EnableConfigurationProperties(OfficeProperties :: class)
@ComponentScan
class OfficeAutoConfiguration {

    @Autowired
    private lateinit var officeProperties: OfficeProperties

    /**
     * 创建pdf转换器
     * TODO: 目前只有一个转换器
     */
    @Bean
    @ConditionalOnMissingBean
    fun documentConverter(): DocumentConverter {
        if (officeProperties.pdfConverter == DocumentConverterConstants.OPEN_OFFICE.value) {
            return OpenOfficeDocumentConverter(this.officeProperties.openOffice)
        }
        return OpenOfficeDocumentConverter(this.officeProperties.openOffice)
    }
}