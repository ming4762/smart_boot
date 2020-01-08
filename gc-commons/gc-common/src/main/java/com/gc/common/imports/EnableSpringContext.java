package com.gc.common.imports;

import com.gc.common.spring.ApplicationContextRegister;
import org.springframework.context.annotation.Import;

/**
 * spring容器引入类
 * @author jackson
 */
@Import(ApplicationContextRegister.class)
public @interface EnableSpringContext {
}
