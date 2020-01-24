package com.gc.module.code.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代码生成器模块mapper注入类
 * @author jackson
 * 2020/1/21 10:15 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@MapperScan(basePackages = "com.gc.module.code.mapper")
public @interface CodeMapperScan {

    @AliasFor(annotation = MapperScan.class, attribute = "sqlSessionTemplateRef")
    String sqlSessionTemplateRef() default "";

    @AliasFor(annotation = MapperScan.class, attribute = "sqlSessionFactoryRef")
    String sqlSessionFactoryRef() default "";
}
