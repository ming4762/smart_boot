package com.gc.starter.log.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shizhongming
 * 2020/6/24 8:54 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@MapperScan(basePackages = "com.gc.starter.log.mapper")
public @interface MapperScanLog {

    @AliasFor(annotation = MapperScan.class, attribute = "sqlSessionTemplateRef")
    String sqlSessionTemplateRef() default "";

    @AliasFor(annotation = MapperScan.class, attribute = "sqlSessionFactoryRef")
    String sqlSessionFactoryRef() default "";
}
