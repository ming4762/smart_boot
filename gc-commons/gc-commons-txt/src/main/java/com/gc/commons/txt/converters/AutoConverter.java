package com.gc.commons.txt.converters;

/**
 * 标识是自动转换器
 * @author shizhongming
 * 2020/7/8 3:34 下午
 */
public class AutoConverter implements Converter<Object> {
    @Override
    public Object convertToJavaData(String data) {
        return null;
    }

    @Override
    public String convertToTxtData(Object value) {
        return null;
    }
}
