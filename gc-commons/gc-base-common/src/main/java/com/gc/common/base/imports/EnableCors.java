package com.gc.common.base.imports;

import com.gc.common.base.filter.CorsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jackson
 * 2020/2/16 3:29 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CorsConfiguration.class)
public @interface EnableCors {
}
