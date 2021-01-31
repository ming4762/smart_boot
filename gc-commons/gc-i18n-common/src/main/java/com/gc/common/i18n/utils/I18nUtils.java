package com.gc.common.i18n.utils;

import com.gc.common.base.message.I18nMessage;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.util.Assert;

import java.util.Locale;
import java.util.Objects;
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

    /**
     * 通过Key获取I18N信息
     * @param key key
     * @return I18N信息
     */
    public static String get(String key) {
        validate();
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    /**
     * 获取I18N信息
     * @param i18nMessage I18nMessage
     * @return I18N
     */
    public static String get(I18nMessage i18nMessage) {
        validate();
        return messageSource.getMessage(i18nMessage.getI18nCode(), null, LocaleContextHolder.getLocale());
    }

    /**
     * 获取I18N信息
     * @param i18nMessage I18nMessage
     * @return I18N
     */
    public static String get(I18nMessage i18nMessage, Object[] args) {
        validate();
        return messageSource.getMessage(i18nMessage.getI18nCode(), args, LocaleContextHolder.getLocale());
    }

    /**
     * 获取I18N信息
     * @param i18nMessage I18nMessage
     * @return I18N
     */
    public static String get(I18nMessage i18nMessage, Object[] args, Locale locale) {
        validate();
        return messageSource.getMessage(i18nMessage.getI18nCode(), args, locale);
    }

    /**
     * 判断是否支持I18N
     * @return 是否支持I18N
     */
    public static boolean supportI18n() {
       return Objects.nonNull(messageSource) && !(messageSource instanceof DelegatingMessageSource && Objects.isNull(((DelegatingMessageSource) messageSource).getParentMessageSource()));
    }

    /**
     * 获取I18N MessageSource
     * @return MessageSource
     */
    public static MessageSource getMessageSource() {
        return messageSource;
    }

    private static void validate() {
        Assert.notNull(messageSource, "messageSource cannot be null");
    }

    public static void setMessageSource(MessageSource messageSource) {
        I18nUtils.messageSource = messageSource;
    }
}
