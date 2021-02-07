package com.gc.common.i18n.reader;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * 资源读取器
 * 读取I18N资源
 * @author ShiZhongMing
 * 2021/1/31 11:37
 * @since 1.0
 */
public interface ResourceReader {

    /**
     * 读取资源
     * @param locale 语言
     * @return 资源信息
     */
    Map<String, String> read(Locale locale) throws IOException;
}
