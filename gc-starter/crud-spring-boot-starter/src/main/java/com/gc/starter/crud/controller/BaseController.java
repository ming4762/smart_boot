package com.gc.starter.crud.controller;

import com.gc.common.base.message.Result;
import com.gc.starter.crud.model.BaseModel;
import com.gc.starter.crud.service.BaseService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * 基础controller
 * @author shizhongming
 * 2020/1/12 8:35 下午
 */
public abstract class BaseController<K extends BaseService<T>, T extends BaseModel> extends BaseQueryController<K, T> {


    /**
     * 删除接口
     */
    protected Result<Integer> delete(@RequestBody T model) {
        return Result.success(this.service.delete(model));
    }

    /**
     * 添加更新
     */
    protected Result<Boolean> saveUpdate(@RequestBody T model) {
        return Result.success(this.service.saveOrUpdate(model));
    }

    /**
     * 保存
     */
    protected Result<Boolean> save(@RequestBody T model) {
        return Result.success(this.service.save(model));
    }

    /**
     * 更新
     */
    protected Result<Boolean> update(@RequestBody T model) {
        return Result.success(this.service.updateById(model));
    }

    /**
     * 批量删除
     * @param modelList
     * @return
     */
    protected Result<Integer> batchDelete(@RequestBody List<T> modelList) {
        return Result.success(this.service.batchDelete(modelList));
    }

    /**
     * 批量保存
     */
    protected Result<Boolean> batchSave(@RequestBody List<T> modelList) {
        return Result.success(this.service.saveBatch(modelList));
    }

    /**
     * 批量保存/更新
     */
    protected Result<Boolean> batchSaveUpdate(@RequestBody List<T> modelList) {
        return Result.success(this.service.saveOrUpdateBatch(modelList));
    }

    /**
     * 获取request对象
     * @return request对象
     */
    @Nullable
    public HttpServletRequest getRequest() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    /**
     * 获取Response对象
     * @return Response对象
     */
    @Nullable
    public HttpServletResponse getResponse() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }
}
