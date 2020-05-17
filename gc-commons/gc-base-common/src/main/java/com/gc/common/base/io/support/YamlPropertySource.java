package com.gc.common.base.io.support;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * yaml 加载器
 * @author shizhongming
 * 2020/5/15 3:35 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PropertySource(value = {""}, factory = YamlPropertyLoaderFactory.class)
public @interface YamlPropertySource {

    @AliasFor(annotation = PropertySource.class, attribute = "value")
    String[] value();
}
