package com.gc.starter.i18n.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ShiZhongMing
 * 2021/1/28 17:39
 * @since 1.0
 */
@Configuration
public class ValidatorConfiguration implements WebMvcConfigurer {

    private final MessageSource messageSource;

    public ValidatorConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Validator getValidator() {
        return this.i18nValidator();
    }

    @Bean
    public Validator i18nValidator() {
        final LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }
}
