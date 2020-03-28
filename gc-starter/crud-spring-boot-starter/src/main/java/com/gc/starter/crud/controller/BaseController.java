package com.gc.starter.crud.controller;

import com.gc.common.base.message.Result;
import com.gc.starter.crud.model.BaseModel;
import com.gc.starter.crud.service.BaseService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
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
}
