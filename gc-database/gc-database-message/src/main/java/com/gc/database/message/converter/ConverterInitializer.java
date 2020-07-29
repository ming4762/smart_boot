package com.gc.database.message.converter;

import com.gc.database.message.utils.CacheUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 转换器初始化器
 * @author shizhongming
 * 2020/7/29 8:48 下午
 */
public class ConverterInitializer implements InitializingBean {

    private final ConverterProvider converterProvider;

    public ConverterInitializer(ConverterProvider converterProvider) {
        this.converterProvider = converterProvider;
    }

    @Override
    public void afterPropertiesSet() {
        this.initAutoConverter();
    }

    /**
     * 初始化自动转换器
     */
    private void initAutoConverter() {
        this.converterProvider.getConverter().forEach(item -> {
            // 获取接口类型
            final Type[] types = item.getClass().getGenericInterfaces();
            for (Type type : types) {
                if (type instanceof ParameterizedType && StringUtils.equals(((ParameterizedType) type).getRawType().getTypeName(), Converter.class.getName())) {
                    final ParameterizedType parameterizedType = (ParameterizedType) type;
                    // 将接口中的两个类型拼接到一起作为key
                    String key = Arrays.stream(parameterizedType.getActualTypeArguments())
                            .map(Type :: getTypeName)
                            .collect(Collectors.joining());
                    CacheUtils.addConverter(key, item);
                    break;
                }
            }
        });
    }
}
