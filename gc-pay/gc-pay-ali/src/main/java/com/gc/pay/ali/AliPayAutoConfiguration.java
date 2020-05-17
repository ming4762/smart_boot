package com.gc.pay.ali;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author shizhongming
 * 2020/5/15 5:35 下午
 */
@ComponentScan
@Configuration
@EnableConfigurationProperties(AliPayProperties.class)
public class AliPayAutoConfiguration extends WebMvcConfigurationSupport {

}
