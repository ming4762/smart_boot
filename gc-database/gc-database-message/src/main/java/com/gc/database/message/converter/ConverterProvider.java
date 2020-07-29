package com.gc.database.message.converter;

import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 转换器提供接口
 * @author shizhongming
 * 2020/7/29 8:41 下午
 */
public interface ConverterProvider {

    /**
     * 获取转换器
     * @author shizhongming
     * @return 转换器列表
     */
    @NonNull
    List<Converter> getConverter();
}
