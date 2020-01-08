package com.gc.common.utils;

import com.google.common.collect.Sets;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * string 工具类
 * @author shizhongming
 * 2020/1/8 8:11 下午
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 下划线改为驼峰表示
     * @param camelCaseName
     * @return
     */
    @NotNull
    public static String underscoreName(@NotNull String camelCaseName) {
        final var pattern = Pattern.compile("[A-Z]");
        final var matcher = pattern.matcher(camelCaseName);
        var stringBuffer = new StringBuffer(camelCaseName);
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
