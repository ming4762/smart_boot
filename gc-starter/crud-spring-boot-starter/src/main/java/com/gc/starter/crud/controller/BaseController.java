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
//    @RequestMapping("/delete")
//    @ResponseBody
    protected Result<Integer> delete(@RequestBody T model) {
        return Result.success(this.service.delete(model));
    }

    /**
     * 添加更新
     */
//    @RequestMapping("/saveUpdate")
//    @ResponseBody
    protected Result<Boolean> saveUpdate(@RequestBody T model) throws Exception {
        return Result.success(this.service.saveOrUpdate(model));
    }

    /**
     * 保存
     */
//    @RequestMapping("/save")
//    @ResponseBody
    protected Result<Boolean> save(@RequestBody T model) throws Exception {
        return Result.success(this.service.save(model));
    }

    /**
     * 更新
     */
//    @RequestMapping("/update")
//    @ResponseBody
    protected Result<Boolean> update(@RequestBody T model) throws Exception {
        return Result.success(this.service.updateById(model));
    }

    /**
     * 批量删除
     * @param modelList
     * @return
     */
//    @RequestMapping("/batchDelete")
//    @ResponseBody
    protected Result<Integer> batchDelete(@RequestBody List<T> modelList) throws Exception {
        return Result.success(this.service.batchDelete(modelList));
    }

    /**
     * 批量保存
     */
//    @RequestMapping("/batchSave")
//    @ResponseBody
    protected Result<Boolean> batchSave(@RequestBody List<T> modelList) {
        return Result.success(this.service.saveBatch(modelList));
    }

    /**
     * 批量保存/更新
     */
//    @RequestMapping("/batchSaveUpdate")
//    @ResponseBody
    protected Result<Boolean> batchSaveUpdate(@RequestBody List<T> modelList) {
        return Result.success(this.service.saveOrUpdateBatch(modelList));
    }
}
