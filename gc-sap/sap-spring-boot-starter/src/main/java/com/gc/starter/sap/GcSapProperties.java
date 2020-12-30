package com.gc.starter.sap;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SAP 配置类
 * @author ShiZhongMing
 * 2020/12/25 14:42
 * @since 1.0
 */
@ConfigurationProperties("gc.sap")
@Getter
@Setter
public class GcSapProperties {

    private String jcoAshost;
    private String jcoSaprouter;
    private String jcoSysnr;
    private String jcoClient;
    private String jcoUser;
    private String jcoPasswd;
    private String jcoLang;
}
