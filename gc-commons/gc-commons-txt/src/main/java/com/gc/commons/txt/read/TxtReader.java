package com.gc.commons.txt.read;

import com.gc.common.base.utils.ReflectUtil;
import com.gc.commons.txt.CacheUtils;
import com.gc.commons.txt.TxtBaseModel;
import com.gc.commons.txt.annotation.TxtProperty;
import com.gc.commons.txt.converters.Converter;
import com.gc.commons.txt.converters.ConverterHolder;
import com.gc.commons.txt.read.listener.ReadListener;
import com.gc.commons.txt.read.parameter.ReadParameter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shizhongming
 * 2020/7/7 11:05 下午
 */
@Slf4j
public class TxtReader {


    private final TxtReadAnalysis txtReadAnalysis;

    public TxtReader() {
        this.txtReadAnalysis = new DefaultTxtReadAnalysis();
    }

    /**
     * 执行读取
     * @param readParameter 读取参数
     */
    public <T extends TxtBaseModel> void read(ReadParameter<T> readParameter) throws IOException {
        // 获取监听器
        ReadListener<T> readListener = readParameter.getReadListener();
        // 获取编码方式
        final Charset charset = ObjectUtils.firstNonNull(readParameter.getCharset(), StandardCharsets.UTF_8);
        // 计数器
        AtomicInteger atomicInteger = new AtomicInteger(0);
        try (final LineIterator lineIterator = IOUtils.lineIterator(readParameter.getInputStream(), charset)) {
            while (lineIterator.hasNext()) {
                try {
                    // 读取行
                    String line = readListener.formatLine(lineIterator.nextLine());
                    // 判断头部位置
                    int index = atomicInteger.getAndIncrement();
                    if (index < readParameter.getFirstRow() || !readListener.readLine(line)) {
                        continue;
                    }
                    // 判断是否有下一行
                    if (!readListener.hasNext(line)) {
                        break;
                    }
                    // 解析内容
                    LinkedHashMap<String, String> result = this.txtReadAnalysis.readLine(line, readListener.separator(), new ArrayList<>());
                    T txtBaseModel = this.convertToBean(readParameter.getClazz(), result);
                    readListener.invoke(txtBaseModel);
                } catch (Exception e) {
                    readListener.onException(e);
                }
            }
            readListener.doAfterAllAnalysed();
        }
    }

    @SneakyThrows
    private <T extends TxtBaseModel> T convertToBean(Class<T> modelClass, LinkedHashMap<String, String> data) {
        final T result = modelClass.getDeclaredConstructor().newInstance();
        // 获取value list
        final List<String> valueList = new ArrayList<>(data.values());
        // todo: 实体类继续处理
        for (Field field : modelClass.getDeclaredFields()) {
            // 获取 TxtProperty
            final TxtProperty txtProperty = CacheUtils.getTxtProperty(field);
            if (ReflectUtil.isStatic(field) || Objects.isNull(txtProperty)) {
                continue;
            }
            Method method = CacheUtils.getWriteMethod(field);
            // 获取值
            String value = null;
            final String headerKey = txtProperty.value();
            int index = txtProperty.index();
            if (StringUtils.isNotBlank(headerKey)) {
                value = data.get(headerKey);
            } else if (index != -1 && index < valueList.size()) {
                // TODO: 数据超界
                value = valueList.get(index);
            }
            if (StringUtils.isNotBlank(value)) {
                Object convertValue = value;
                // 获取转换器
                if (!Objects.equals(field.getType(), String.class)) {
                    final Converter<?> converter = ConverterHolder.getConverter(field);
                    convertValue = converter.convertToJavaData(value);
                }
                method.invoke(result, convertValue);
            }
        }
        return result;
    }
}
