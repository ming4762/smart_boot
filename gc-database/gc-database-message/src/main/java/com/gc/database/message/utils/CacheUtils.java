package com.gc.database.message.utils;

import com.gc.database.message.constants.TypeMappingConstant;
import com.gc.database.message.converter.Converter;
import com.gc.database.message.pojo.dbo.AbstractDatabaseBaseDO;
import com.google.common.collect.Maps;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存工具类
 * @author shizhongming
 * 2020/1/18 9:05 下午
 */
public class CacheUtils {

    private CacheUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 数据库连接缓存
     */
    private static final ConcurrentMap<String, Connection> CONNECTION_CACHE = Maps.newConcurrentMap();

    /**
     * 数据库字段与Field映射缓存
     */
    private static final ConcurrentMap<Class<? extends AbstractDatabaseBaseDO>, Map<String, Field>> DATABASE_FIELD_MAPPING = Maps.newConcurrentMap();

    /**
     * 类型映射
     */
    private static final ConcurrentMap<Integer, TypeMappingConstant> TYPE_MAPPING_CACHE = Maps.newConcurrentMap();

    /**
     * converter缓存
     */
    private static final ConcurrentMap<Class<? extends Converter>, Converter> CONVERTER_CACHE = Maps.newConcurrentMap();

    /**
     * 自动转换器存储
     */
    private static final ConcurrentMap<String, Converter> AUTO_CONVERTER_CACHE = Maps.newConcurrentMap();


    /**
     * 添加converter
     * @param key key
     * @param converter converter
     */
    public static void addConverter(@NonNull String key, @NonNull Converter converter) {
        AUTO_CONVERTER_CACHE.put(key, converter);
    }

    /**
     * 获取自动转换器
     * @param key 转换器的key
     * @return 自动转换器
     */
    @Nullable
    public static Converter getAutoConverter(String key) {
        return AUTO_CONVERTER_CACHE.get(key);
    }


    /**
     * 缓存数据库连接
     * @param key key
     * @param connection 数据库连接
     */
    public static void setConnectionCache(@NonNull String key, @NonNull Connection connection) {
        CONNECTION_CACHE.put(key, connection);
    }

    @Nullable
    public static Connection getConnection(String key) {
        return CONNECTION_CACHE.get(key);
    }

    public static void removeConnection(@NonNull String key) {
        CONNECTION_CACHE.remove(key);
    }


    public static void setFieldMapping(@NonNull Class<? extends AbstractDatabaseBaseDO> key, @NonNull Map<String, Field> mapping) {
        DATABASE_FIELD_MAPPING.put(key, mapping);
    }

    @Nullable
    public static Map<String, Field> getFieldMapping(@NonNull Class<? extends AbstractDatabaseBaseDO> key) {
        return DATABASE_FIELD_MAPPING.get(key);
    }

    /**
     * 获取转换器
     * @param clazz 转换器类型
     * @return 转换器
     * @throws NoSuchMethodException 未找到构造方法
     * @throws IllegalAccessException IllegalAccessException
     * @throws InvocationTargetException InvocationTargetException
     * @throws InstantiationException InstantiationException
     */
    public static Converter getConverter(Class<? extends Converter> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!CONVERTER_CACHE.containsKey(clazz)) {
            CONVERTER_CACHE.put(clazz, clazz.getDeclaredConstructor().newInstance());
        }
        return CONVERTER_CACHE.get(clazz);
    }

    /**
     * 数据库field映射是否为null
     * @return
     */
    public static boolean isFieldMappingEmpty() {
        return DATABASE_FIELD_MAPPING.isEmpty();
    }

    public static void setTypeMapping(@NonNull Integer key, @NonNull TypeMappingConstant mapping) {
        TYPE_MAPPING_CACHE.put(key, mapping);
    }

    public static void setTypeMapping(@NonNull Map<Integer, TypeMappingConstant> values) {
        TYPE_MAPPING_CACHE.putAll(values);
    }

    @Nullable
    public static TypeMappingConstant getFieldMapping(@NonNull Integer key) {
        return TYPE_MAPPING_CACHE.get(key);
    }
}
