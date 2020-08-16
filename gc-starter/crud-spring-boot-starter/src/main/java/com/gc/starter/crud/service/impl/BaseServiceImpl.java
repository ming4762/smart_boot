package com.gc.starter.crud.service.impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gc.common.base.exception.IllegalAccessRuntimeException;
import com.gc.common.base.exception.IntrospectionRuntimeException;
import com.gc.common.base.exception.InvocationTargetRuntimeException;
import com.gc.common.base.exception.NoSuchMethodRuntimeException;
import com.gc.common.base.utils.ReflectUtil;
import com.gc.starter.crud.constants.UserPropertyConstants;
import com.gc.starter.crud.mapper.CrudBaseMapper;
import com.gc.starter.crud.model.BaseModel;
import com.gc.starter.crud.model.Sort;
import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.crud.service.BaseService;
import com.gc.starter.crud.utils.CrudUtils;
import com.gc.starter.crud.utils.IdGenerator;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/10 9:51 下午
 */
public abstract class BaseServiceImpl<K extends CrudBaseMapper<T>, T extends BaseModel> extends ServiceImpl<K, T> implements BaseService<T> {

    private static final String SORT_ASC = "ASC";

    private static final String KEY_PROPERTY_NULL_ERROR = "error: can not execute. because can not find column for id from entity!";

    /**
     * 通过实体删除
     * @param model 实体类
     * @return 删除的数量
     */
    @Override
    @NonNull
    public Integer delete(@NonNull T model) {
        final List<String> keyList = this.getKeyList();
        if (keyList.isEmpty()) {
            log.warn("未找到主键无法执行删除");
            return 0;
        }
        if (keyList.size() == 1) {
            return this.baseMapper.deleteById((Serializable) ReflectUtil.getFieldValue(model, keyList.get(0)));
        }
        final Class<? extends BaseModel> clazz = CrudUtils.getModelClassByType(this.getModelType());
        if (clazz == null) {
            return 0;
        }
        final QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        keyList.forEach(key -> queryWrapper.eq(CrudUtils.getDbField(clazz, key), ReflectUtil.getFieldValue(model, key)));
        return this.baseMapper.delete(queryWrapper);
    }

    /**
     * 通过实体类批量删除
     * @param modelList 包含key信息的实体类
     * @return 删除数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public @NonNull Integer batchDelete(@NonNull List<T> modelList) {
        int num;
        final List<String> keyList = this.getKeyList();
        if (keyList.isEmpty()) {
            log.warn("未找到实体类主键，无法执行删除");
            return 0;
        }
        if (keyList.size() == 1) {
            final String key = keyList.get(0);
            final Set<Serializable> keyValueList = modelList
                    .stream()
                    .map(model -> (Serializable)ReflectUtil.getFieldValue(model, key))
                    .collect(Collectors.toSet());
            num = this.baseMapper.deleteBatchIds(keyValueList);
        } else {
            num = modelList
                    .stream()
                    .mapToInt(this::delete)
                    .sum();
        }
        return num;
    }

    /**
     * 重写批量删除方法，如果ID只有一个调用removeById方法
     * @param idList ID列表
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (idList.size() == 1) {
            return this.removeById(idList.iterator().next());
        }
        return super.removeByIds(idList);
    }

    /**
     * 查询单一实体
     * @param model 包含key信息的实体类
     * @return 实体类
     */
    @Nullable
    @Override
    public T get(@NonNull T model) {
        final List<String> keyList = this.getKeyList();
        if (keyList.isEmpty()) {
            return null;
        }
        if (keyList.size() == 1) {
            return this.getById((Serializable) ReflectUtil.getFieldValue(model, keyList.get(0)));
        } else {
            final Class<? extends BaseModel> clazz = CrudUtils.getModelClassByType(this.getModelType());
            if (clazz == null) {
                return null;
            }
            final QueryWrapper<T> queryWrapper = new QueryWrapper<>();
            keyList.forEach(key -> queryWrapper.eq(CrudUtils.getDbField(clazz, key), ReflectUtil.getFieldValue(model, key)));
            return this.getOne(queryWrapper);
        }
    }

    /**
     * 重写save方法，修改ID的生成策略
     * @author jackson
     * @param entity 实体类
     * @return 是否保存成功
     */
    @Override
    public boolean save(@NonNull T entity) {
        Class<?> cls = entity.getClass();
        // 获取key
        final TableInfo tableInfo = this.getTableInfo();
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, KEY_PROPERTY_NULL_ERROR);
        Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
        if (StringUtils.checkValNull(idVal)) {
            // 如果ID为null 手动设置ID
            this.setNumberId(entity, tableInfo);
        }
        return super.save(entity);
    }

    /**
     * 重写批量save方法，修改ID的生成策略
     * @author jackson
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        if (ObjectUtils.isEmpty(entityList)) {
            return false;
        }
        // 获取实体类tableInfo
        final TableInfo tableInfo = this.getTableInfo();
        Class<? extends BaseModel> clazz = this.getModelClass();
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, KEY_PROPERTY_NULL_ERROR);
        // 遍历实体类设置主键
        entityList.forEach(entity -> {
            Object idVal = ReflectionKit.getMethodValue(clazz, entity, keyProperty);
            if (StringUtils.checkValNull(idVal)) {
                // 如果ID为null 手动设置ID
                this.setNumberId(entity, tableInfo);
            }
        });
        return super.saveBatch(entityList, batchSize);
    }

    /**
     * 获取 TableInfo
     * @return 实体类TableInfo信息
     */
    @NonNull
    private TableInfo getTableInfo() {
        final TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getModelClass());
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        return tableInfo;
    }


    /**
     * 设置number类型的ID
     * 修改主键生成策略
     * @param entity 实体类
     * @param tableInfo 表信息
     */
    private void setNumberId(@NonNull T entity, @NonNull TableInfo tableInfo) {
        final IdType idType = tableInfo.getIdType();
        if (idType.getKey() == IdType.ASSIGN_ID.getKey() && Number.class.isAssignableFrom(tableInfo.getKeyType())) {
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(tableInfo.getKeyProperty(), entity.getClass());
                propertyDescriptor.getWriteMethod().invoke(entity, IdGenerator.nextId());
            } catch (IllegalAccessException e) {
                throw new IllegalAccessRuntimeException(e);
            } catch (IntrospectionException e) {
                throw new IntrospectionRuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new InvocationTargetRuntimeException(e);
            }
        }
    }

    /**
     * 重写方法
     * 防止idList为空时发生错误
     * @param idList 实体类ID列表
     * @return 实体类集合
     */
    @Override
    public List<T> listByIds(Collection<? extends Serializable> idList) {
        if (idList.isEmpty()) {
            return Lists.newArrayList();
        }
        final Set<? extends Serializable> idSet = new HashSet<>(idList);
        if (idSet.size() == 1) {
            final T result = this.getById(idSet.iterator().next());
            return result == null ? Lists.newArrayList() : Lists.newArrayList(result);
        }
        return super.listByIds(idList);
    }

    /**
     * 查询实体函数
     * @param queryWrapper 查询对象
     * @param parameter 参数
     * @param paging 是否分页
     * @return 查询结果
     */
    @Override
    public @NonNull List<T> list(@NonNull QueryWrapper<T> queryWrapper, @NonNull PageQueryParameter<String, Object> parameter, @NonNull Boolean paging) {
        this.analysisOrder(queryWrapper, parameter, paging);
        return super.list(queryWrapper);
    }

    /**
     * 插入更新带有创建用户
     * @param model 实体
     * @return 是否成功
     */
    @Override
    public boolean saveOrUpdateWithCreateUser(@NonNull T model, Long userId) {
        boolean isAdd = this.isAdd(model);
        if (isAdd) {
            return this.saveWithUser(model, userId);
        } else {
            return this.updateById(model);
        }
    }

    /**
     * 插入更新带有更新人员
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    public boolean saveOrUpdateWithUpdateUser(@NonNull T model, Long userId) {
        boolean isAdd = this.isAdd(model);
        if (isAdd) {
            return this.save(model);
        } else {
            return this.updateWithUserById(model, userId);
        }
    }

    /**
     * 插入更新带有所有人员
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    public boolean saveOrUpdateWithAllUser(@NonNull T model, Long userId) {
        boolean isAdd = this.isAdd(model);
        if (isAdd) {
            return this.saveWithUser(model, userId);
        } else {
            return this.updateWithUserById(model, userId);
        }
    }

    /**
     * 保存带有创建人员信息
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否保存成功
     */
    @Override
    public boolean saveWithUser(@NonNull T model, Long userId) {
        this.setCreateUserId(model, userId);
        this.setCreateTime(model);
        return this.save(model);
    }



    @Override
    public boolean updateWithUserById(@NonNull T model, Long userId) {
        this.setUpdateTime(model);
        this.setUpdateUserId(model, userId);
        return updateById(model);
    }

    /**
     * 解析排序字段
     * @param queryWrapper 查询类
     * @param parameter 参数
     * @param paging 是否分页
     */
    public void analysisOrder(@NonNull QueryWrapper<T> queryWrapper, @NonNull PageQueryParameter<String, Object> parameter, boolean paging) {
        final String sortName = parameter.getSortName();
        // 如果灭有分页且存在排序字段手动进行排序
        if (!paging && sortName != null) {
            final String sortOrder = parameter.getSortOrder();
            final Class<? extends BaseModel> clazz = CrudUtils.getModelClassByType(this.getModelType());
            final List<Sort> sortList = CrudUtils.analysisOrder(sortName, sortOrder, clazz);
            if (!sortList.isEmpty()) {
                sortList.forEach(sort -> {
                    if (SORT_ASC.equalsIgnoreCase(sort.getOrder())) {
                        queryWrapper.orderByAsc(sort.getDbName());
                    } else {
                        queryWrapper.orderByDesc(sort.getDbName());
                    }
                });
            }
        }
    }

    /**
     * 获取实体类类型
     * @return 实体类类型
     */
    @NonNull
    private Type getModelType() {
        return ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * 获取实体类的类型
     * @return 实体类类型
     */
    private Class<? extends BaseModel> getModelClass() {
        return CrudUtils.getModelClassByType(this.getModelType());
    }

    /**
     * 获取数据库表的key
     * @return 数据库表key列表
     */
    private List<String> getKeyList() {
        final List<Field> keyList = CrudUtils.getModelKeysByType(this.getModelType());
        return keyList
                .stream()
                .map(Field::getName)
                .collect(Collectors.toList());

    }

    /**
     * 判断是否是保存操作
     * @param entity 实体类
     * @return 是否执行保存
     */
    protected boolean isAdd(@NonNull T entity) {
        Serializable key = this.getKeyValue(entity);
        return StringUtils.checkValNull(key) || Objects.isNull(this.getById(key));
    }

    /**
     * 设置创建用户ID
     * @param model 实体类
     * @param userId 用户ID
     */
    private void setCreateUserId(T model, Long userId) {
        try {
            PropertyUtils.setProperty(model, UserPropertyConstants.CREATE_USER_ID.getName(), userId);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(e);
        }
    }

    /**
     * 设置创建时间
     * @param model 实体类
     */
    private void setCreateTime(T model) {
        try {
            PropertyUtils.setProperty(model, UserPropertyConstants.CREATE_TIME.getName(), new Date());
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(e);
        }
    }

    private void setUpdateUserId(T model, Long userId) {
        try {
            PropertyUtils.setProperty(model, UserPropertyConstants.UPDATE_USER_ID.getName(), userId);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(e);
        }
    }

    private void setUpdateTime(T model) {
        try {
            PropertyUtils.setProperty(model, UserPropertyConstants.UPDATE_TIME.getName(), new Date());
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(e);
        }
    }

    /**
     * 获取主键的值
     * @param entity 实体类
     * @return key
     */
    private Serializable getKeyValue(@NonNull T entity) {
        Class<?> cls = entity.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, KEY_PROPERTY_NULL_ERROR);
        return (Serializable) ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
    }
}
