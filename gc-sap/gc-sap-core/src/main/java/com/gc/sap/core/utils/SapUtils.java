package com.gc.sap.core.utils;

import com.gc.common.base.exception.*;
import com.gc.sap.core.annotation.SapField;
import com.gc.sap.core.model.SapModelField;
import com.google.common.collect.Lists;
import com.sap.conn.jco.JCoTable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * SAP 工具类
 * @author ShiZhongMing
 * 2020/12/25 8:58
 * @since 1.0
 */
public class SapUtils {


    private SapUtils() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * 从SapTable获取数据
     * @param table saptable
     * @param clazz 转换的类
     * @param <T> 实体类类型
     * @author shizhongming
     * @return 数据
     */
    public static <T> List<T> getDataFromSapTable(JCoTable table, Class<T> clazz) {
        try {
            // 获取实体类SAP属性信息
            final List<SapModelField> sapModelFieldList = SapModelCache.getSapModelFieldList(clazz);
            if (CollectionUtils.isEmpty(sapModelFieldList)) {
                return Lists.newArrayList();
            }
            List<T> data = new ArrayList<>(table.getNumRows());
            do {
                // 循环添加每一条数据
                data.add(SapUtils.createSapModel(clazz, sapModelFieldList, table));
            } while (table.nextRow());

            return data;
        } catch (IntrospectionException e) {
            throw new IntrospectionRuntimeException(e);
        }
    }

    /**
     * 创建SAP实体类
     * @param clazz 实体类类型
     * @param sapModelFieldList 列信息
     * @param table sap table
     * @param <T> 实体类类型
     * @return 实体类
     */
    private static <T> T createSapModel(Class<T> clazz, List<SapModelField> sapModelFieldList, JCoTable table) {
        try {
            // 初始化实体类
            T t = clazz.getDeclaredConstructor().newInstance();
            for (SapModelField sapModelField : sapModelFieldList) {
                final Field field = sapModelField.getField();
                // 获取属性值
                final SapField sapField = sapModelField.getSapField();
                // 设置属性值
                sapModelField.getPropertyDescriptor().getWriteMethod().invoke(t, SapUtils.getSapValue(table, sapField, field));

            }
            return t;
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(e);
        } catch (InstantiationException e) {
            throw new InstantiationRuntimeException(e);
        }
    }

    /**
     * 获取sap值
     * @param table sap table
     * @param sapField 列信息
     * @return sap值
     */
    private static Object getSapValue(JCoTable table, SapField sapField, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 获取方法
        String methodName = "get" + field.getType().getSimpleName();
        if (sapField.index() != -1) {
            final Method method = JCoTable.class.getMethod(methodName, int.class);
            return method.invoke(table, sapField.index());
        }
        String fieldName = field.getName().toUpperCase();
        if (StringUtils.isNotBlank(sapField.value())) {
            fieldName = sapField.value();
        }
        // 根据属性名获取值
        final Method method = JCoTable.class.getMethod(methodName, String.class);
        return method.invoke(table, fieldName);
    }
}
