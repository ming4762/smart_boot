package com.gc.commons.txt.read;

import org.springframework.lang.NonNull;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * TXT读取执行器
 * @author shizhongming
 * 2020/7/8 3:03 下午
 */
public interface TxtReadAnalysis {

    /**
     * 读取行
     * @param line 行数据
     * @param separator 分隔符
     * @param headerList 头部信息
     * @return 读取的数据
     */
    @NonNull
    LinkedHashMap<String, String> readLine(String line, String separator, List<String> headerList);


}
