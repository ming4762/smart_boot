package com.gc.starter.async.imports;

import com.gc.starter.async.timeout.TimeoutConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shizhongming
 * 2020/5/28 2:55 下午
 */
@Import(TimeoutConfig.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableTimeout {
}
