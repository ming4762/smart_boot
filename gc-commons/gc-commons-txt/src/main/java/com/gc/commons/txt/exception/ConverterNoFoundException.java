package com.gc.commons.txt.exception;

import java.lang.reflect.Field;

/**
 * @author shizhongming
 * 2020/7/8 3:28 下午
 */
public class ConverterNoFoundException extends RuntimeException {
    private static final long serialVersionUID = 4999592897602888590L;

    public ConverterNoFoundException(Field field) {
        super(String.format("未找到转换器,Field:%s", field.toString()));
    }
}
