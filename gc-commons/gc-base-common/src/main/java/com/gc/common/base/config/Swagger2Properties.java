package com.gc.common.base.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jackson
 * 2020/1/30 5:44 下午
 */
@ConfigurationProperties("gc.api")
@Getter
@Setter
public class Swagger2Properties {

    private String title = "";

    private String description = "";

    private String version;

    private String license;

    private String licenseUrl;

    private String termsOfServiceUrl;

    private String basePackage = "com";

    private Contact contact = new Contact();

    @Getter
    @Setter
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }
}
