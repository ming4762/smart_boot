package com.gc.database.message.converter;

import com.google.common.collect.Lists;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 默认的转换器提供类
 * @author shizhongming
 * 2020/7/29 8:44 下午
 */
public class DefaultConverterProvider implements ConverterProvider {

    /**
     * 获取转换器
     * @author shizhongming
     * @return 转换器列表
     */
    @Override
    @NonNull
    public List<Converter> getConverter() {
        return Lists.newArrayList(new BigDecimalToIntegerConverter());
    }
}
