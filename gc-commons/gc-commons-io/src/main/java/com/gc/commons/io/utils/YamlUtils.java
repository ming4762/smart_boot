package com.gc.commons.io.utils;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

/**
 * Yaml 工具类
 * @author shizhongming
 * 2021/1/30 11:56 下午
 */
@Slf4j
public class YamlUtils {

    private YamlUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Yaml createYaml() {
        return new Yaml();
    }

    /**
     * 读取YML 文件
     * @param reader 输入流
     * @return 读取的数据
     */
    public static Map<String, Object> read(Reader reader) {
        final Yaml yaml = new Yaml();
        return yaml.loadAs(reader, Map.class);
    }

    /**
     * 读取YML文件 并将结果转为一层Map
     * @param reader 输入流
     * @return 读取并转换的数据
     */
    public static Map<String, Object> readInOneLayer(Reader reader) {
        Map<String, Object> data = read(reader);
        Map<String, Object> result = Maps.newHashMap();
        convertInOneLayer(data, "", result);
        return result;
    }

    private static void convertInOneLayer(Map<String, Object> data, String parentKey, Map<String, Object> result) {
        data.forEach((key, value) -> {
            String nextKey = StringUtils.isEmpty(parentKey) ? key : String.join(".", parentKey, key);
            if (value instanceof Map) {
                // 继续执行
                convertInOneLayer((Map<String, Object>) value, nextKey, result);
            } else {
                // 没有下一级添加到结果内容
                result.put(nextKey, value);
            }
        });
    }
}
