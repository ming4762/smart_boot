package com.gc.starter.i18n;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

/**
 * @author shizhongming
 * 2020/5/12 5:56 下午
 */
@Getter
@Setter
@ConfigurationProperties("gc.i18n")
public class I18nProperties {

    /**
     * 默认的Locale
     */
    private Locale defaultLocale;
}
