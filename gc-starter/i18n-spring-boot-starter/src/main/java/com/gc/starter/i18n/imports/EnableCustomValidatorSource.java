package com.gc.starter.i18n.imports;

import com.gc.starter.i18n.config.ValidatorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义ValidatorSource
 * @author ShiZhongMing
 * 2021/1/28 17:42
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ValidatorConfiguration.class)
public @interface EnableCustomValidatorSource {
}
