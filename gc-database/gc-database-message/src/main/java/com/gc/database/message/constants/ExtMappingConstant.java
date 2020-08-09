package com.gc.database.message.constants;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * extjs 与 java映射
 * @author shizhongming
 * 2020/7/30 8:39 下午
 */
@Getter
public enum ExtMappingConstant {
    /**
     * extjs 与 java映射
     */
    BOOLEAN("boolean", Boolean.class),
    INTEGER("int", Integer.class),
    LONG("int", Long.class),
    DOUBLE("number", Double.class),
    FLOAT("number", Float.class),
    BIGD_ECIMAL("number", java.math.BigDecimal.class),
    LOCAL_DATE("date", LocalDate.class),
    LOCAL_TIME("date", LocalTime.class),
    LOCAL_DATE_TIME("date", LocalDateTime.class),
    STRING("string", String.class);

    private final String extClass;

    private final Class<?> javaClass;

    ExtMappingConstant(String extClass, Class<?> javaClass) {
        this.extClass = extClass;
        this.javaClass = javaClass;
    }
}
