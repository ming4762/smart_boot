package com.gc.starter.i18n;

import com.gc.common.i18n.utils.I18nUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author shizhongming
 * 2020/5/12 5:57 下午
 */
@Configuration
@EnableConfigurationProperties(I18nProperties.class)
public class I18nAutoConfiguration {

    private final MessageSource messageSource;

    public I18nAutoConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    public void init() {
        I18nUtils.setMessageSource(this.messageSource);
    }

}
