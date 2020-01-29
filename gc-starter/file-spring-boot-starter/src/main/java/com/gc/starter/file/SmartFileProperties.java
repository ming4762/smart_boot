package com.gc.starter.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shizhongming
 * 2020/1/15 9:03 下午
 */
@ConfigurationProperties(prefix = "gc.file")
@Getter
@Setter
public class SmartFileProperties {

    private String actuatorType = ActuatorTypeEnum.mongoDB.name();

    private String basePath;
}
