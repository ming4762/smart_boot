package com.gc.commons.txt.converters;

import com.gc.commons.txt.CacheUtils;
import com.gc.commons.txt.annotation.TxtProperty;
import com.gc.commons.txt.exception.ConverterNoFoundException;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shizhongming
 * 2020/7/8 3:21 下午
 */
public class ConverterHolder {

    private static final Map<Class<?>, Converter<?>> ALL_CONVERTER = new ConcurrentHashMap<>();

    private static final Map<Field, Converter<?>> FIELD_CONVERTER_MAP = new ConcurrentHashMap<>();

    private ConverterHolder() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 加载所有转换器
     */
    public static void loadAllConverter() {
        if (ALL_CONVERTER.isEmpty()) {
            ALL_CONVERTER.put(Double.class, new DoubleConverter());
            ALL_CONVERTER.put(String.class, new StringConverter());
            ALL_CONVERTER.put(Integer.class, new IntegerConverter());
            ALL_CONVERTER.put(BigDecimal.class, new BigDecimalConverter());
        }
    }

    /**
     * 添加转换器
     * @param clazz 转换器类型
     * @param converter 转换器
     */
    public static void addConverter(Class<?> clazz, Converter<?> converter) {
        ALL_CONVERTER.put(clazz, converter);
    }

    /**
     * 获取转换器
     * @param field 属性
     * @return 转换器
     */
    @NonNull
    public static Converter<?> getConverter(@NonNull Field field) {
        loadAllConverter();
        createCustomConverter(field);
        // 获取自定义转换器
        Converter<?> converter = FIELD_CONVERTER_MAP.get(field);
        if (Objects.isNull(converter)) {
            converter = ALL_CONVERTER.get(field.getType());
        }
        if (Objects.isNull(converter)) {
            throw new ConverterNoFoundException(field);
        }
        return converter;
    }

    /**
     * 创建自定义的转换器
     * @param field 属性
     */
    @SneakyThrows
    private static void createCustomConverter(@NonNull Field field)  {
        final TxtProperty txtProperty = CacheUtils.getTxtProperty(field);
        if (Objects.nonNull(txtProperty) && !Objects.equals(txtProperty.converter(), AutoConverter.class)) {
            // 获取转换器
            Converter<?> converter = txtProperty.converter().getDeclaredConstructor().newInstance();
            FIELD_CONVERTER_MAP.put(field, converter);
        }
    }
}
