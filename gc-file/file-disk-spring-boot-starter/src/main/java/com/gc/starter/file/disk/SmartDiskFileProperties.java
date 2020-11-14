package com.gc.starter.file.disk;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shizhongming
 * 2020/11/5 10:52 下午
 */
@ConfigurationProperties(prefix = "gc.file")
@Getter
@Setter
public class SmartDiskFileProperties {

    /**
     * 文件存储基础路径
     */
    private String basePath;
}
