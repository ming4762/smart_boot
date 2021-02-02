package com.gc.common.i18n.format;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shizhongming
 * 2021/2/1 11:54 下午
 */
@Slf4j
public class DefaultMessageFormat implements MessageFormat {

    private static final Pattern PATTERN = Pattern.compile("\\{.*?}");

    @Override
    public String format(String formValue, Map<String, Object> args) {
        final Matcher matcher = PATTERN.matcher(formValue);
        final List<String> matchValues = Lists.newArrayList();
        while (matcher.find()) {
            matchValues.add(matcher.group());
        }
        return null;
    }

    @Override
    public String format(String formValue, Object[] args) {
        return null;
    }

    public static void main(String[] args) {
        new DefaultMessageFormat().format("你好啊{a},这是{adf}{ad1}", Maps.newHashMap());
    }
}
