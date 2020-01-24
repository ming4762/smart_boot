package com.gc.common.base.imports;

import com.gc.common.base.spring.ApplicationContextRegister;
import org.springframework.context.annotation.Import;

/**
 * spring容器引入类
 * @author jackson
 */
@Import(ApplicationContextRegister.class)
public @interface EnableSpringContext {
}
