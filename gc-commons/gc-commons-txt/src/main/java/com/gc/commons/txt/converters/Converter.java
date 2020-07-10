package com.gc.commons.txt.converters;

/**
 * 转换接口
 * @author shizhongming
 * 2020/7/7 10:44 下午
 */
public interface Converter<T> {

    /**
     * 转为java数据
     * @param data 源数据
     * @return 转换后的数据
     */
    T convertToJavaData(String data);

    /**
     * 转为Txt数据
     * @param value java值
     * @return txt值
     */
    String convertToTxtData(T value);
}
