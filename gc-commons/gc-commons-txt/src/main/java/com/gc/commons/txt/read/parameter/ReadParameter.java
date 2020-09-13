package com.gc.commons.txt.read.parameter;

import com.gc.commons.txt.TxtBaseModel;
import com.gc.commons.txt.read.TxtReadAnalysis;
import com.gc.commons.txt.read.listener.ReadListener;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author shizhongming
 * 2020/7/7 11:07 下午
 */
@Getter
@Setter
public class ReadParameter<T extends TxtBaseModel> {

    /**
     * 文件数据流
     */
    private InputStream inputStream;

    /**
     * 实体类类型
     */
    private Class<T> clazz;

    private ReadListener<T> readListener;

    /**
     * 数据第一行的行数
     */
    @NonNull
    private Integer firstRow = 0;

    /**
     * 编码方式
     */
    private Charset charset;

    private TxtReadAnalysis txtReadAnalysis;
}
