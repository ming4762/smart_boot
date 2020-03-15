package com.gc.starter.crud.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.starter.crud.model.BaseModel;
import com.gc.starter.crud.model.Sort;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gc.common.base.utils.ExceptionUtils;
import com.gc.common.base.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Crud工具类
 * @author shizhongming
 * 2020/1/10 10:00 下午
 */
@Slf4j
public final class CrudUtils {

    private static final ImmutableMap<String, SymbolParameterType> WRAPPER_METHOD_PARAMETER_MAP= ImmutableMap.<String, SymbolParameterType>builder()
            .put("=", new SymbolParameterType("eq", new Class[]{Object.class, Object.class}))
            .put("like", new SymbolParameterType("like", new Class[]{Object.class, Object.class}))
            .put(">", new SymbolParameterType("gt", new Class[]{Object.class, Object.class}))
            .put(">=", new SymbolParameterType("ge", new Class[]{Object.class, Object.class}))
            .put("<>", new SymbolParameterType("ne", new Class[]{Object.class, Object.class}))
            .put("<", new SymbolParameterType("lt", new Class[]{Object.class, Object.class}))
            .put("<=", new SymbolParameterType("le", new Class[]{Object.class, Object.class}))
            .put("in", new SymbolParameterType("in", new Class[]{Object.class, Collection.class}))
            .put("notLike", new SymbolParameterType("notLike", new Class[]{Object.class, Object.class}))
            .put("likeLeft", new SymbolParameterType("likeLeft", new Class[]{Object.class, Object.class}))
            .put("likeRight", new SymbolParameterType("likeRight", new Class[]{Object.class, Object.class}))
            .put("notIn", new SymbolParameterType("notIn", new Class[]{Object.class, Collection.class}))
            .put("groupBy", new SymbolParameterType("groupBy", new Class[]{Object.class}))
            .build();

    private static final String SYMBOL_EQUAL = "=";

    private static final String SYMBOL_NOT_EQUAL = "<>";

    /**
     * class-keys缓存
     */
    private static final Map<Class<? extends BaseModel>, List<Field>> CLASS_KEY_CACHE = Maps.newConcurrentMap();

    /**
     * type-class 缓存
     */
    private static final Map<Type, Class<? extends BaseModel>> TYPE_CLASS_CACHE = Maps.newConcurrentMap();

    /**
     * field-数据库字段 缓存
     */
    private static final Map<Field, String> FIELD_DBFIELD_CACHE = Maps.newConcurrentMap();

    /**
     * 通过Class获取实体类的key
     */
    @NotNull
    public static List<Field> getModelKeysByClass(@NotNull Class<? extends BaseModel> clazz) {
        if (CLASS_KEY_CACHE.containsKey(clazz)) {
            return CLASS_KEY_CACHE.get(clazz);
        }
        final List<Field> fieldList = Lists.newLinkedList();
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    if (field.getAnnotation(TableId.class) != null) {
                        fieldList.add(field);
                    }
                });
        CLASS_KEY_CACHE.put(clazz, fieldList);
        return CLASS_KEY_CACHE.get(clazz);
    }

    /**
     * 通过类型获取实体类key属性
     * @param type
     * @return
     */
    @NotNull
    public static List<Field> getModelKeysByType(@NotNull Type type) {
        Class<? extends BaseModel> clazz = getModelClassByType(type);
        if (clazz == null) {
            return Lists.newArrayList();
        }
        return getModelKeysByClass(clazz);
    }

    /**
     * 通过类型获取实体类Class
     * @param type
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nullable
    public static Class<? extends BaseModel> getModelClassByType(@NotNull Type type) {
        if (TYPE_CLASS_CACHE.containsKey(type)) {
            return TYPE_CLASS_CACHE.get(type);
        }
        Class<? extends BaseModel> classGet = null;
        try {
            final Class clazz = Class.forName(type.getTypeName());
            if (BaseModel.class.isAssignableFrom(clazz)) {
                classGet = clazz;
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        TYPE_CLASS_CACHE.put(type, classGet);
        return TYPE_CLASS_CACHE.get(type);
    }

    /**
     * 解析排序字段
     * @param sortName 以逗号分隔的实体类属性名称
     * @param sortOrder 以逗号分隔的排序方法
     * @param clazz 实体类类型
     * @return
     */
    @NotNull
    public static List<Sort> analysisOrder(@NotNull String sortName, @Nullable String sortOrder, Class<? extends BaseModel> clazz) {
        final String[] sortNameList = sortName.split(",");
        final List<String> sortOrderList = sortOrder == null ? new ArrayList<String>() : Arrays.asList(sortOrder.split(","));
        final List<Sort> sortList = new LinkedList<Sort>();
        for (int i=0; i<sortNameList.length; i++) {
            final String name = sortNameList[i].trim();
            final String order = sortOrderList.size() > i ? sortOrderList.get(i).trim() : "asc";
            // 获取数据库字段
            final String dbName = CrudUtils.getDbField(clazz, name);
            if (org.springframework.util.StringUtils.isEmpty(dbName)) {
                log.warn("未找到排序字段对应的数据库字段：{}，该排序属性被忽略", name);
            } else {
                sortList.add(new Sort(name, order, dbName));
            }
        }
        return sortList;
    }

    /**
     * 通过实体类类型、实体类属性名称获取数据库字段名
     * @param clazz 实体类类型
     * @param fieldName 实体类属性名称
     * @return 数据库字段名
     */
    @Nullable
    public static String getDbField(@NotNull Class<? extends BaseModel> clazz, @NotNull String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
        }
        if (field != null) {
            return getDbField(field);
        }
        return null;
    }

    /**
     * 根据实体类属性获取数据库字段名称
     * 1、优先从注解获取
     * 2、如果没有注解，属性名去除驼峰表示作为数据库字段
     */
    @NotNull
    public static String getDbField(@NotNull Field field) {
        String dbField = FIELD_DBFIELD_CACHE.get(field);
        if (dbField == null) {
            TableId tableIdField = field.getAnnotation(TableId.class);
            if (tableIdField != null) {
                dbField = tableIdField.value();
            }
            if (org.springframework.util.StringUtils.isEmpty(dbField)) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null) {
                    dbField = tableField.value();
                }
            }
            if (org.springframework.util.StringUtils.isEmpty(dbField)) {
                // 如果未指定数据库字段，通过驼峰表示获取
                dbField = getDefaultDbField(field.getName());
            }
            FIELD_DBFIELD_CACHE.put(field, dbField);
        }
        return FIELD_DBFIELD_CACHE.get(field);
    }

    /**
     * 获取默认的数据库字段
     * @param fieldName
     * @return
     */
    @NotNull
    public static String getDefaultDbField(@NotNull String fieldName) {
        return StringUtils.underscoreName(fieldName);
    }

    /**
     * 从参数创建QueryWrapper
     * @param parameter
     * @param type
     * @param <T>
     * @return
     */
    @NotNull
    public static <T extends BaseModel> QueryWrapper<T> createQueryWrapperFromParameters(@NotNull Map<String, Object> parameter, @NotNull Type type) {
        final Class<? extends BaseModel> clazz = getModelClassByType(type);
        if (clazz != null) {
            return createQueryWrapperFromParameters(parameter, clazz);
        } else {
            log.warn("未找到实体类类型");
        }
        return new QueryWrapper<T>();
    }

    /**
     * 从参数创建QueryWrapper
     * @param parameter
     * @param clazz
     * @param <T>
     * @return
     */
    @NotNull
    public static <T extends BaseModel> QueryWrapper<T> createQueryWrapperFromParameters(@NotNull Map<String, Object> parameter, @NotNull Class<? extends BaseModel> clazz) {
        final QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
        createBaseQueryWrapperFromParameters(parameter, clazz, queryWrapper);
        return queryWrapper;
    }

    private static <T extends BaseModel> void createBaseQueryWrapperFromParameters(@NotNull Map<String, Object> parameter, @NotNull Class<? extends BaseModel> clazz, @NotNull Wrapper<T> queryWrapper) {
        final String symbolSplit = "@";
        parameter.forEach((key, value) -> {
            if (key.contains(symbolSplit)) {
                final String[] keySplit = key.split(symbolSplit);
                // 获取符号
                final String symbol = keySplit.length > 1 ? keySplit[1] : null;
                if (org.springframework.util.StringUtils.isEmpty(symbol)) {
                    log.warn("参数无效，为找到符号，key:{}", key);
                } else {
                    final String fieldName = getDbField(clazz, keySplit[0]);
                    if (fieldName == null) {
                        log.warn("参数无效，未找到实体类对应属性：{}", keySplit[0]);
                    } else {
                        final String dbFieldName = getDbField(clazz, fieldName);
                        if (value != null) {
                            // 值不为null处理
                            if (org.apache.commons.lang3.StringUtils.isNotEmpty(value.toString())) {
                                final Method method = getWrapperMethod(queryWrapper.getClass(), symbol);
                                if (method == null) {
                                    log.warn("参数无效，未找到符号对应执行方法：{}", symbol);
                                } else {
                                    try {
                                        method.invoke(queryWrapper, dbFieldName, value);
                                    } catch (Exception e) {
                                        ExceptionUtils.doThrow(e);
                                    }
                                }
                            } else {
                                log.warn("参数无效，忽略参数值：{}", value);
                            }
                        } else {
                            // null 处理
                            if (org.apache.commons.lang3.StringUtils.equals(SYMBOL_EQUAL, symbol)) {
                                ((QueryWrapper<T>) queryWrapper).isNull(dbFieldName);
                            } else if (org.apache.commons.lang3.StringUtils.equals(symbol, SYMBOL_NOT_EQUAL)) {
                                ((QueryWrapper<T>) queryWrapper).isNotNull(dbFieldName);
                            } else {
                                log.warn("null值参数只能使用'='或'<>'");
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取wrapper执行方法
     * @param clazz
     * @param symbol
     * @return
     */
    private static Method getWrapperMethod(Class clazz, String symbol) {
        Method method = null;
        final SymbolParameterType symbolParameterType = WRAPPER_METHOD_PARAMETER_MAP.get(symbol);
        if (symbolParameterType != null) {
            try {
                method = clazz.getMethod(symbolParameterType.getSymbol(), symbolParameterType.getParameterTypes());
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage(), e);
            }
        }
        return method;
    }


    @AllArgsConstructor
    @Getter
    @Setter
    static class SymbolParameterType {
        private String symbol;

        private Class[] parameterTypes;
    }

}
