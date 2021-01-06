package com.gc.auth.extensions.saml2;

import com.gc.auth.extensions.saml2.config.AuthSaml2BeanConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.saml.SAMLBootstrap;

/**
 * Saml2 自动配置类
 * @author ShiZhongMing
 * 2021/1/6 12:15
 * @since 1.0
 */
@Configuration
@Import(AuthSaml2BeanConfiguration.class)
public class AuthSaml2AutoConfiguration {

    /**
     * Initialization of OpenSAML library
     * @return SAMLBootstrap
     */
    @Bean
    @ConditionalOnMissingBean(SAMLBootstrap.class)
    public SAMLBootstrap samlBootstrap() {
        return new SAMLBootstrap();
    }

}
