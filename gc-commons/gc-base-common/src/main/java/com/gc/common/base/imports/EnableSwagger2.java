package com.gc.common.base.imports;

import com.gc.common.base.config.Swagger2Config;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 引入swagger2配置
 * @author jackson
 * 2020/1/30 5:54 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(Swagger2Config.class)
public @interface EnableSwagger2 {
}
