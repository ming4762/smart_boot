package com.gc.common.base.utils;

import org.springframework.lang.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * string 工具类
 * @author shizhongming
 * 2020/1/8 8:11 下午
 */
public final class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final Pattern PATTERN = Pattern.compile("[A-Z]");

    private static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

    /**
     * 驼峰标识改为下划线
     * @param camelCaseName 源字符串
     * @return 改为下划线的字符串
     */
    @NonNull
    public static String humpToLine(@NonNull String camelCaseName) {
        final Matcher matcher = PATTERN.matcher(camelCaseName);
        StringBuffer stringBuffer = new StringBuffer(camelCaseName);
        if (matcher.find()) {
            stringBuffer = new StringBuffer();
            matcher.appendReplacement(stringBuffer,"_"+matcher.group(0).toLowerCase());
            matcher.appendTail(stringBuffer);
        } else {
            return stringBuffer.toString();
        }
        return humpToLine(stringBuffer.toString());
    }

    /**
     * 下划线转驼峰
     * @param str 源字符串
     * @return 改为驼峰标识的字符串
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
