package com.gc.common.base.imports;

import com.gc.common.base.config.ValidatorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 全局校验引入类
 * @author jackson
 * 2020/3/12 9:55 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ValidatorConfig.class)
public @interface EnableGlobalValidator {
}
