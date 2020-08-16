package com.gc.commons.txt.read;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认的读取解析器
 * @author shizhongming
 * 2020/7/8 3:07 下午
 */
public class DefaultTxtReadAnalysis implements TxtReadAnalysis {

    /**
     * 读取行
     * @param line 行数据
     * @param separator 分隔符
     * @param headerList 头部信息
     * @return 读取的数据
     */
    @Override
    @NonNull
    public LinkedHashMap<String, String> readLine(String line, String separator, List<String> headerList) {
        String[] values = StringUtils.split(line, separator);
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Arrays.asList(values).forEach(item -> {
            // 获取头部信息
            int index = atomicInteger.incrementAndGet();
            String header = "header_" + index;
            if (headerList.size() > (index + 1)) {
                header = headerList.get(index);
            }
            result.put(header, item.trim());
        });
        return result;
    }
}
