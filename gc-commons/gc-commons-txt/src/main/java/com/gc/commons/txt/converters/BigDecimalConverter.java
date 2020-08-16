package com.gc.commons.txt.converters;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * BigDecimal 转换器
 * @author shizhongming
 * 2020/8/16 11:35 上午
 */
public class BigDecimalConverter implements Converter<BigDecimal> {
    @Override
    public BigDecimal convertToJavaData(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return new BigDecimal(data);
    }

    @Override
    public String convertToTxtData(BigDecimal value) {
        if (Objects.isNull(value)) {
            return "";
        }
        return value.toString();
    }
}
