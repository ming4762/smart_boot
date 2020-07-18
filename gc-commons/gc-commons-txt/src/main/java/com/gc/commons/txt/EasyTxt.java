package com.gc.commons.txt;

import com.gc.commons.txt.read.builder.TxtReaderBuilder;
import com.gc.commons.txt.read.listener.ReadListener;
import org.springframework.lang.NonNull;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 入口类
 * @author shizhongming
 * 2020/7/7 10:46 下午
 */
public class EasyTxt {

    /**
     * 读取TXT
     * @param inputStream 输入流
     * @param clazz 实体列
     * @param readListener 读取监听
     * @return TxtReaderBuilder
     */
    public static <T extends TxtBaseModel> TxtReaderBuilder<T> read(@NonNull InputStream inputStream, @NonNull Class<T> clazz, @NonNull ReadListener<T> readListener) {
        return read(inputStream, clazz, readListener, StandardCharsets.UTF_8);
    }

    /**
     * 读取TXT
     * @param inputStream 输入流
     * @param clazz 实体列
     * @param readListener 读取监听
     * @param charset 编码方式
     * @return TxtReaderBuilder
     */
    public static <T extends TxtBaseModel> TxtReaderBuilder<T> read(@NonNull InputStream inputStream, @NonNull Class<T> clazz, @NonNull ReadListener<T> readListener, @NonNull Charset charset) {
        final TxtReaderBuilder<T> txtReaderBuilder = new TxtReaderBuilder<>();
        txtReaderBuilder
                .file(inputStream)
                .modelType(clazz)
                .charset(charset)
                .listener(readListener);
        return txtReaderBuilder;
    }
}
