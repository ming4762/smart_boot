package com.gc.database.message.converter;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * BigDecimal 转为 Integer
 * @author ShiZhongMing
 * 2020/7/25 12:27
 * @since 0.0.6
 */
public class BigDecimalToIntegerConverter implements Converter<BigDecimal, Integer> {
    @Override
    public Integer convert(BigDecimal resource) {
        // 将BigDecimal 转为Integer
        return Optional.ofNullable(resource)
                .map(BigDecimal :: toString)
                .map(Integer :: parseInt)
                .orElse(null);
    }
}
