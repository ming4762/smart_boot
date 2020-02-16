package com.gc.common.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.common.base.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * json 工具类
 * @author jackson
 * 2020/2/15 8:48 下午
 */
@Slf4j
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 对象转为json
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BaseException("json转换发生异常", e);
        }
    }

    /**
     * json转为对象
     * @param json
     * @return
     */
    public static Object parse(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            throw new BaseException("json转换发生异常", e);
        }
    }

    /**
     * json转为对象
     * @param json
     * @return
     */
    public static <T>  T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BaseException("json转换发生异常", e);
        }
    }
}
