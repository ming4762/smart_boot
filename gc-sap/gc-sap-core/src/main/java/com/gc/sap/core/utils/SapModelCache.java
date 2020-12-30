package com.gc.sap.core.utils;


import com.gc.sap.core.annotation.SapField;
import com.gc.sap.core.model.SapModelField;
import com.google.common.collect.Lists;
import org.springframework.core.annotation.AnnotationUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SAP 实体类缓存
 * @author ShiZhongMing
 * 2020/12/25 9:58
 * @since 1.0
 */
public class SapModelCache {

    /**
     * 缓存
     */
    private static final ConcurrentHashMap<Class<?>, List<SapModelField>> MODEL_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取SAP FIELDlist
     * @param clazz 实体类
     * @return SAP FIELDlist
     */
    public static List<SapModelField> getSapModelFieldList(Class<?> clazz) throws IntrospectionException {
        if (MODEL_CACHE.containsKey(clazz)) {
            return MODEL_CACHE.get(clazz);
        }
        final List<SapModelField> sapModelFieldList = Lists.newArrayList();
        final Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // 判断 Field是否有SapField注解
            final SapField sapField = AnnotationUtils.findAnnotation(field, SapField.class);
            if (Objects.isNull(sapField)) {
                continue;
            }
            // 获取属性描述
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
            // 创建 sapModelField
            final SapModelField sapModelField = new SapModelField();
            sapModelField.setField(field);
            sapModelField.setSapField(sapField);
            sapModelField.setPropertyDescriptor(propertyDescriptor);
            sapModelFieldList.add(sapModelField);
        }
        // 添加缓存
        MODEL_CACHE.put(clazz, sapModelFieldList);
        return sapModelFieldList;
    }
}
