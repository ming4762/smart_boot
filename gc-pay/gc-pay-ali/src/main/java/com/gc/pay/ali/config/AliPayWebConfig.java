package com.gc.pay.ali.config;

import com.gc.pay.ali.AliPayProperties;
import com.gc.pay.ali.interceptor.AliPayInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shizhongming
 * 2020/5/16 9:59 下午
 */
@Configuration
public class AliPayWebConfig implements WebMvcConfigurer {

    private final AliPayProperties payProperties;

    public AliPayWebConfig(AliPayProperties payProperties) {
        this.payProperties = payProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AliPayInterceptor(this.payProperties)).addPathPatterns("/aliPay/**");
    }
}
