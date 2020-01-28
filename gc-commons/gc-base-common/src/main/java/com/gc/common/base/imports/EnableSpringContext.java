package com.gc.common.base.imports;

import com.gc.common.base.spring.ApplicationContextRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * spring容器引入类
 * @author jackson
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ApplicationContextRegister.class)
public @interface EnableSpringContext {
}
