package com.gc.common.base.imports;

import com.gc.common.base.config.JacksonConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shizhongming
 * 2020/9/13 5:51 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JacksonConfig.class)
public @interface EnableJacksonConfig {
}
