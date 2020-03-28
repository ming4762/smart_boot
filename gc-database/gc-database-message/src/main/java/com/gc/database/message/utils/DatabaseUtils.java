package com.gc.database.message.utils;

import com.gc.common.base.exception.*;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据库工具类
 * @author shizhongming
 * 2020/1/19 8:41 下午
 */
public class DatabaseUtils {

    private DatabaseUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * resultSet转为实体类
     * @param resultSet
     * @param clazz
     * @param mapping
     * @param <T>
     * @return
     */
    @NotNull
    public static <T> List<T> resultSetToModel(@NotNull ResultSet resultSet, @NotNull Class<T> clazz, @NotNull Map<String, Field> mapping) {
        try {
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = metaData.getColumnCount();
            final List<T> modelList = Lists.newLinkedList();
            while (resultSet.next()) {
                final T model = clazz.getDeclaredConstructor().newInstance();
                for (int i=1; i<=columnCount; i++) {
                    final String name = metaData.getColumnName(i);
                    final Field field = mapping.get(name);
                    if (field != null) {
                        Object value;
                        final Class aClass = field.getType();
                        if (aClass == Date.class) {
                            value = resultSet.getTimestamp(i);
                        } else if (aClass == Short.class) {
                            value = resultSet.getShort(i);
                        } else {
                            value = resultSet.getObject(i);
                        }
                        field.set(model, value);
                    }
                }
                modelList.add(model);
            }
            return modelList;
        } catch (InstantiationException e) {
           throw new InstantiationRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(e);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(e);
        }
    }
}
