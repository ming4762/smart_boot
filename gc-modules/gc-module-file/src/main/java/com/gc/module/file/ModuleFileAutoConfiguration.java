package com.gc.module.file;

import com.gc.common.base.constants.SqlSessionTemplateConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author jacksons
 * 2020/1/27 12:37 下午
 */
@Configuration
@ComponentScan
@MapperScan(value = "com.gc.module.file.mapper", sqlSessionTemplateRef = SqlSessionTemplateConstants.SYSTEM_TEMPLATE)
public class ModuleFileAutoConfiguration {
}
