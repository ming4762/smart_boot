package com.smart.starter.log.annotation

/**
 * 日志注解，添加该注解的类或方法实现日志
 * 目前只支持controller层
 * @author zhongming
 * @since 3.0
 * 2018/5/29下午5:04
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Log(val value: String = "")
