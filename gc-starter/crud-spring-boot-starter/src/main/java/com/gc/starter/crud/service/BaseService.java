package com.gc.starter.crud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gc.starter.crud.model.BaseModel;
import com.gc.starter.crud.query.PageQueryParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 基础serivce层
 * @author shizhongming
 * 2020/1/10 9:50 下午
 */
public interface BaseService<T extends BaseModel> extends IService<T> {

    /**
     * 通过实体删除
     * @param model 实体类
     * @return 删除的数量
     */
    @NotNull
    Integer delete(@NotNull T model);

    /**
     * 通过实体类批量删除
     * @param modelList 包含key信息的实体类
     * @return 删除数量
     */
    @NotNull
    Integer batchDelete(@NotNull List<T> modelList);

    /**
     * 查询单一实体
     * @param model 包含key信息的实体类
     * @return 实体类
     */
    @Nullable
    T get(@NotNull T model);

    /**
     * 插入更新带有创建用户
     * @param model 实体
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean saveOrUpdateWithCreateUser(@NotNull T model, Long userId);

    /**
     * 插入更新带有更新人员
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean saveOrUpdateWithUpdateUser(@NotNull T model, Long userId);

    /**
     * 插入更新带有所有人员
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean saveOrUpdateWithAllUser(@NotNull T model, Long userId);

    /**
     * 保存带有创建人员信息
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否保存成功
     */
    boolean saveWithUser(@NotNull T model, Long userId);

    /**
     * 更新带有更新人员
     * @param model 实体类
     * @param userId 人员信息
     * @return 是否更新成功
     */
    boolean updateWithUserById(@NotNull T model, Long userId);

    /**
     * 查询函数
     * @param queryWrapper 查询参数
     * @param parameter 参数
     * @param paging 是否分页
     * @return 查询结果
     */
    @NotNull
    List<T> list(@NotNull QueryWrapper<T> queryWrapper, @NotNull PageQueryParameter<String, Object> parameter, @NotNull Boolean paging);
}
