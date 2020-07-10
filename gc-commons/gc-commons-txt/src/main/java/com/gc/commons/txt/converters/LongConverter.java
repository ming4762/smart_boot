package com.gc.commons.txt.converters;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author shizhongming
 * 2020/7/8 5:34 下午
 */
public class LongConverter implements Converter<Long> {
    @Override
    public Long convertToJavaData(String data) {
        return StringUtils.isNotBlank(data) ? Long.valueOf(data) : null;
    }

    @Override
    public String convertToTxtData(Long value) {
        return Objects.isNull(value) ? "" : value.toString();
    }
}
