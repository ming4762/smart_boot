package com.gc.starter.crud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gc.starter.crud.model.BaseModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 基础serivce层
 * @author shizhongming
 * 2020/1/10 9:50 下午
 */
public interface BaseService<T extends BaseModel> extends IService<T> {

    @NotNull
    Integer delete(@NotNull T model);

    @NotNull
    Integer batchDelete(@NotNull List<T> modelList);

    @Nullable
    T get(@NotNull T model);

    /**
     * 插入更新带有创建用户
     * @param model
     * @return
     */
    boolean saveOrUpdateWithCreateUser(@NotNull T model, Long userId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    /**
     * 插入更新带有更新人员
     * @param model
     * @param userId
     * @return
     */
    boolean saveOrUpdateWithUpdateUser(@NotNull T model, Long userId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    /**
     * 插入更新带有所有人员
     * @param model
     * @param userId
     * @return
     */
    boolean saveOrUpdateWithAllUser(@NotNull T model, Long userId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    boolean saveWithUser(@NotNull T model, Long userId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    boolean updateWithUserById(@NotNull T model, Long userId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    @NotNull
    List<T> list(@NotNull QueryWrapper<T> queryWrapper, @NotNull Map<String, Object> parameter, @NotNull Boolean paging);
}
