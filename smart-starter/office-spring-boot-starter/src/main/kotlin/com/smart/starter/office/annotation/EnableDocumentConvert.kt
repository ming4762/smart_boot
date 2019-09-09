package com.smart.starter.office.annotation

import com.smart.starter.office.OfficeAutoConfiguration
import org.springframework.context.annotation.Import

/**
 * 文档转换配置启动注解
 */
@Target(AnnotationTarget.CLASS)
@Retention
@Import(OfficeAutoConfiguration :: class)
@MustBeDocumented
annotation class EnableDocumentConvert {

}