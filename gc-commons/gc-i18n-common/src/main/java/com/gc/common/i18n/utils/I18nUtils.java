package com.gc.common.i18n.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * I18N工具类
 * @author shizhongming
 * 2020/5/13 11:03 上午
 */
public class I18nUtils {

    private static MessageSource messageSource;

    /**
     * 通过key获取I18N信息
     * @param key key
     * @return
     */
    public static String get(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public static void setMessageSource(MessageSource messageSource) {
        I18nUtils.messageSource = messageSource;
    }
}
