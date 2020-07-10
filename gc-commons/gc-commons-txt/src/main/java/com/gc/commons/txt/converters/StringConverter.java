package com.gc.commons.txt.converters;

/**
 * @author shizhongming
 * 2020/7/8 4:52 下午
 */
public class StringConverter implements Converter<String> {
    @Override
    public String convertToJavaData(String data) {
        return data;
    }

    @Override
    public String convertToTxtData(String value) {
        return value;
    }
}
