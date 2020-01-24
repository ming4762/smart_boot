package com.gc.common.base.utils;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * string 工具类
 * @author shizhongming
 * 2020/1/8 8:11 下午
 */
public final class StringUtils {

    /**
     * 下划线改为驼峰表示
     * @param camelCaseName
     * @return
     */
    @NotNull
    public static String underscoreName(@NotNull String camelCaseName) {
        final Pattern pattern = Pattern.compile("[A-Z]");
        final Matcher matcher = pattern.matcher(camelCaseName);
        StringBuffer stringBuffer = new StringBuffer(camelCaseName);
        if (matcher.find()) {
            stringBuffer = new StringBuffer();
            matcher.appendReplacement(stringBuffer,"_"+matcher.group(0).toLowerCase());
            matcher.appendTail(stringBuffer);
        } else {
            return stringBuffer.toString();
        }
        return underscoreName(stringBuffer.toString());
    }
}
