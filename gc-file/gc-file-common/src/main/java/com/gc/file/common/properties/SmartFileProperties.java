package com.gc.file.common.properties;

import com.gc.common.jcraft.properties.SmartJcraftProperties;
import com.gc.file.common.constants.ActualFileServiceConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件服务层配置类
 * @author shizhongming
 * 2020/11/29 3:24 上午
 */
@ConfigurationProperties(prefix = "gc.file")
@Getter
@Setter
public class SmartFileProperties {

    /**
     * 文件存储基础路径
     */
    private String basePath;

    /**
     * 默认的文件执行器
     */
    private String defaultHandler = ActualFileServiceConstants.DISK.name();

    /**
     * NFS文件配置
     */
    private SmartJcraftProperties nfs;

}
