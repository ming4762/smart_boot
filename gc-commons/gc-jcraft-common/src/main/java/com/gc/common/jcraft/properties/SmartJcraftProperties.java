package com.gc.common.jcraft.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * NFS文件服务配置
 * @author ShiZhongMing
 * 2020/12/7 15:27
 * @since 1.0
 */
@Getter
@Setter
public class SmartJcraftProperties {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * ip
     */
    private String host;

    private Integer port = 22;

    private String channel = "sftp";

    private String basePath;
}
