package com.gc.common.i18n.provider;

import java.util.Locale;

/**
 * Locale 提供器
 * @author ShiZhongMing
 * 2021/1/31 11:47
 * @since 1.0
 */
public interface LocaleProvider {

    /**
     * 获取 Locale
     * @return Locale
     */
    Locale getLocale();
}
