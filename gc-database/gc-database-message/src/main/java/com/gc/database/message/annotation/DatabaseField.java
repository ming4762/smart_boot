package com.gc.database.message.annotation;

import com.gc.database.message.converter.AutoConverter;
import com.gc.database.message.converter.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识数据库字段与实体类字段的对应关系
 * @author zhongming
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseField {

    String value();

    Class<? extends Converter> converter() default AutoConverter.class;
}
