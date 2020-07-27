package com.gc.database.message.converter;

/**
 * @author ShiZhongMing
 * 2020/7/25 12:26
 * @since 0.0.6
 */
public interface Converter<T, R> {

    /**
     * 进行转换
     * @param resource 源数据
     * @return 转换后的数据
     */
    R convert(T resource);
}
