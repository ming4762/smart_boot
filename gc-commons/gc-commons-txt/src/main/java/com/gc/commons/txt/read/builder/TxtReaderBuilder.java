package com.gc.commons.txt.read.builder;

import com.gc.commons.txt.TxtBaseModel;
import com.gc.commons.txt.read.TxtReader;
import com.gc.commons.txt.read.listener.ReadListener;
import com.gc.commons.txt.read.parameter.ReadParameter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author shizhongming
 * 2020/7/7 10:47 下午
 */
public class TxtReaderBuilder<T extends TxtBaseModel> {

    private final ReadParameter<T> readParameter;

    public TxtReaderBuilder() {
        this.readParameter = new ReadParameter<>();
    }



    /**
     * 设置文件输入流
     * @param inputStream 文件数据流
     * @return this
     */
    public TxtReaderBuilder<T> file(@NonNull InputStream inputStream) {
        this.readParameter.setInputStream(inputStream);
        return this;
    }

    public TxtReaderBuilder<T> modelType(Class<T> modelType) {
        this.readParameter.setClazz(modelType);
        return this;
    }

    public TxtReaderBuilder<T> listener(ReadListener<T> readListener) {
        this.readParameter.setReadListener(readListener);
        return this;
    }

    public TxtReaderBuilder<T> firstRowNum(int rowNumber) {
        this.readParameter.setFirstRow(rowNumber);
        return this;
    }

    /**
     * 设置编码方式
     * @param charset 编码方式
     * @return TxtReaderBuilder
     */
    public TxtReaderBuilder<T> charset(Charset charset) {
        this.readParameter.setCharset(charset);
        return this;
    }

    /**
     * 执行读取
     */
    public void doRead() throws IOException {
        TxtReader txtReader = new TxtReader();
        txtReader.read(this.readParameter);
    }
}
