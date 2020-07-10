package com.gc.commons.txt.converters;

import java.util.Objects;

/**
 * @author shizhongming
 * 2020/7/8 3:18 下午
 */
public class DoubleConverter implements Converter<Double> {
    @Override
    public Double convertToJavaData(String data) {
        return Double.parseDouble(data);
    }

    @Override
    public String convertToTxtData(Double value) {
        return Objects.isNull(value) ? "" : value.toString();
    }
}
