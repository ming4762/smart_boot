package com.gc.common.i18n.source;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * 可重新加载的Message Source
 * @author ShiZhongMing
 * 2021/2/1 8:01
 * @since 1.0
 */
public interface ReloadableMessageSource extends MessageSource {

    /**
     * 重新加载资源文件
     * @param locale Locale
     */
    void reload(Locale locale);

    /**
     * 重新加载资源文件
     */
    void reload();
}
