package com.gc.database.message.converter;

/**
 * 自动转换器
 * @author ShiZhongMing
 * 2020/7/25 12:32
 * @since 1.0
 */
public class AutoConverter implements Converter<Object, Object> {
    @Override
    public Object convert(Object resource) {
        return resource;
    }
}
