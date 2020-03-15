package com.gc.common.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

/**
 * swagger2配置
 * @author jackson
 * 2020/1/30 5:42 下午
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(Swagger2Properties.class)
public class Swagger2Config {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private Swagger2Properties properties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(this.properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        final Contact contact = new Contact(this.properties.getContact().getName(), this.properties.getContact().getUrl(), this.properties.getContact().getEmail());
        return new ApiInfoBuilder()
                .title(this.properties.getTitle())
                .description(this.properties.getDescription())
                .termsOfServiceUrl(this.properties.getTermsOfServiceUrl())
                .contact(contact)
                .license(this.properties.getLicense())
                .licenseUrl(this.properties.getLicenseUrl())
                .version(this.properties.getVersion())
                .build();

    }

}
