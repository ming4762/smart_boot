package com.gc.starter.async.timeout.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 超时注解
 * @author shizhongming
 * 2020/5/28 1:50 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timeout {

    /**
     * 超时时间
     * @return
     */
    long value() default Long.MIN_VALUE;
}
