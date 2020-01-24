package com.gc.database.message.constants;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据库类型java类型映射
 * @author shizhongming
 */
@Getter
public enum TypeMappingConstant {
    BIT(-7, Boolean.class),

    TINYINT(-6, Byte.class),

    SMALLINT(5, Short.class),

    INTEGER(4, Integer.class),

    BIGINT(-5, Long.class),

    FLOAT(6, Double.class),

    REAL(7, Float.class),

    DOUBLE(8, Double.class),

    NUMERIC(2,BigDecimal.class),

    DECIMAL(3, Double.class),

    CHAR(1, String.class),

    VARCHAR(12, String.class),

    LONGVARCHAR(-1, String.class),

    DATE(91, Date.class),

    TIME(92, Date.class),

    TIMESTAMP(93, Date .class),

    BINARY(-2, byte[].class),

    VARBINARY(-3, byte[].class),

    LONGVARBINARY(-4, byte[].class),

    NULL(0, Object.class),

    OTHER(1111, Object.class),

    JAVA_OBJECT(2000, Object.class),

    DISTINCT(2001, Object.class),

    STRUCT(2002, Object.class),

    ARRAY(2003, Object.class),

    BLOB(2004, byte[].class),

    CLOB(2005, String.class),

    REF(2006, Object.class),

    DATALINK(70, String.class),

    BOOLEAN(16, Boolean.class),

    ROWID(-8, String .class),

    NCHAR(-15, String.class),

    NVARCHAR(-9, String.class),

    LONGNVARCHAR(-16, String.class),

    NCLOB(2011, String.class),

    SQLXML(2009, String.class),

    // TODO: 待确认
    REF_CURSOR(2012, String.class),

    // TODO: 待确认
    TIME_WITH_TIMEZONE(2013, Date.class),

    // TODO: 待确认
    TIMESTAMP_WITH_TIMEZONE(2014, Date.class)
    ;

    private Integer dataType;

    private Class<?> javaClass;

    TypeMappingConstant(Integer dataType, Class<?> javaClass) {
        this.dataType = dataType;
        this.javaClass = javaClass;
    }
}
