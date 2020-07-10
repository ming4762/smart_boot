package com.gc.commons.txt.annotation;

import com.gc.commons.txt.converters.AutoConverter;
import com.gc.commons.txt.converters.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记txt字段
 * @author shizhongming
 * 2020/7/7 10:41 下午
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TxtProperty {

    /**
     * 读取的序号
     * @return 序号
     */
    int index() default -1;

    String value() default "";

    Class<? extends Converter> converter() default AutoConverter.class;
}
