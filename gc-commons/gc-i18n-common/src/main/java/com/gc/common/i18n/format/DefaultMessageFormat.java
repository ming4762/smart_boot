package com.gc.common.i18n.format;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认的DefaultMessage
 * @author shizhongming
 * 2021/2/1 11:54 下午
 */
@Slf4j
public class DefaultMessageFormat implements MessageFormat {

    private static final Pattern PATTERN = Pattern.compile("\\{.*?}");

    @Override
    public String format(String formValue, Map<String, Object> args) {
        if (Objects.isNull(args)) {
            return formValue;
        }
        final Matcher matcher = PATTERN.matcher(formValue);
        final Set<String> matchValues = Sets.newHashSet();
        while (matcher.find()) {
            matchValues.add(matcher.group());
        }
        final String[] result = {formValue};
        matchValues.forEach(matchValue -> {
            final String key = StringUtils.remove(StringUtils.remove(matchValue, "{"), "}");
            if (args.containsKey(key)) {
                result[0] = StringUtils.replace(result[0], matchValue, args.get(key).toString());
            }
        });
        return result[0];
    }

    @Override
    public String format(String formValue, Object[] args) {
        if (Objects.isNull(args)) {
            return formValue;
        }
        final Matcher matcher = PATTERN.matcher(formValue);
        final Set<String> matchValues = Sets.newHashSet();
        while (matcher.find()) {
            matchValues.add(matcher.group());
        }
        final String[] result = {formValue};
        matchValues.forEach(matchValue -> {
            final String keyValue = StringUtils.remove(StringUtils.remove(matchValue, "{"), "}");
            final int key = Integer.parseInt(keyValue);
            if (args.length > key) {
                result[0] = StringUtils.replace(result[0], matchValue, args[key].toString());
            }
        });
        return result[0];
    }
}
