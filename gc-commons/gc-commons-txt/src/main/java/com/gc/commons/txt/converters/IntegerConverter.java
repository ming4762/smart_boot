package com.gc.commons.txt.converters;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author shizhongming
 * 2020/7/8 5:03 下午
 */
public class IntegerConverter implements Converter<Integer> {
    @Override
    public Integer convertToJavaData(String data) {
        if (StringUtils.isNotBlank(data)) {
            return Integer.parseInt(data);
        }
        return null;
    }

    @Override
    public String convertToTxtData(Integer value) {
        if (Objects.nonNull(value)) {
            return value.toString();
        }
        return "";
    }
}
