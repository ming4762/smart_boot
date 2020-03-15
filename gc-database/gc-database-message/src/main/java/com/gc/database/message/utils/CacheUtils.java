package com.gc.database.message.utils;

import com.gc.database.message.constants.TypeMappingConstant;
import com.gc.database.message.pojo.bo.AbstractDatabaseBaseBO;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存工具类
 * @author shizhongming
 * 2020/1/18 9:05 下午
 */
public class CacheUtils {

    /**
     * 数据库连接缓存
     */
    public static ConcurrentMap<String, Connection> CONNECTION_CACHE = Maps.newConcurrentMap();

    /**
     * 数据库字段与Field映射缓存
     */
    public static ConcurrentMap<Class<? extends AbstractDatabaseBaseBO>, Map<String, Field>> DATABASE_FIELD_MAPPING = Maps.newConcurrentMap();

    /**
     * 类型映射
     */
    public static ConcurrentMap<Integer, TypeMappingConstant> TYPE_MAPPING_CACHE = Maps.newConcurrentMap();
}
