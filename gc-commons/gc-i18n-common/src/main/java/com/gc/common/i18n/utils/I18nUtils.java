package com.gc.common.i18n.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Optional;

/**
 * I18N工具类
 * @author shizhongming
 * 2020/5/13 11:03 上午
 */
public class I18nUtils {

    private static MessageSource messageSource;

    private I18nUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通过key获取I18N信息
     * @param key key
     * @return I18N信息
     */
    public static String get(String key, String defaultMessage) {
        return Optional.ofNullable(messageSource)
                .map(item -> item.getMessage(key, null, LocaleContextHolder.getLocale()))
                .orElse(defaultMessage);
    }

    public static void setMessageSource(MessageSource messageSource) {
        I18nUtils.messageSource = messageSource;
    }
}
