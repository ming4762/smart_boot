package com.gc.common.i18n.source;

import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;

import java.util.Locale;
import java.util.Map;

/**
 * 支持Map类型的args
 * @author shizhongming
 * 2021/2/1 11:19 下午
 */
public interface MapArgsMessageSource extends MessageSource {

    /**
     * 获取资源信息
     * @param code code
     * @param args args
     * @param defaultMessage defaultMessage
     * @param locale locale
     * @return 资源信息
     */
    String getMessage(String code, @Nullable Map<String, Object> args, @Nullable String defaultMessage, Locale locale);

    /**
     * 获取资源信息
     * @param code code
     * @param args args
     * @param locale locale
     * @return 资源信息
     */
    default String getMessage(String code, @Nullable Map<String, Object> args, Locale locale) {
        return this.getMessage(code, args, null, locale);
    }
}
