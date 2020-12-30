package com.gc.sap.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识SAP字段
 * @author ShiZhongMing
 * 2020/12/25 8:23
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SapField {

    /**
     * sap字段名称
     * @return SAP字段
     */
    String value() default "";

    int index() default -1;
}
