package com.smart.starter.crud.controller;

import com.smart.common.message.Result;
import com.smart.common.message.ResultCodeEnum;
import com.smart.starter.crud.model.BaseModel;
import com.smart.starter.crud.service.BaseService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 解决kotlin List<T> json转换 类型丢失问题
 * @author ming
 * 2019/7/5 下午8:32
 */
public class BaseListParameterController<K extends BaseService<T>, T extends BaseModel> extends BaseControllerQuery<K, T> {

    /**
     * 批量删除
     * @param deleteObjects
     * @return
     */
    @RequestMapping("/batchDelete")
    @ResponseBody
    protected Result<Object> batchDelete(@RequestBody List<T> deleteObjects) {
        try {
            return Result.success(this.service.batchDelete(deleteObjects), ResultCodeEnum.SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("批量删除时发生错误", 0);
        }
    }

    /**
     * 批量保存
     */
    @RequestMapping("/batchSave")
    @ResponseBody
    protected Result<Boolean> batchSave(@RequestBody List<T> tList) {
        try {
            return Result.success(this.service.saveBatch(tList), ResultCodeEnum.SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(e.getMessage(), false);
        }
    }

    /**
     * 批量保存/更新
     */
    @RequestMapping("/batchSaveUpdate")
    @ResponseBody
    protected Result<Boolean> batchSaveUpdate(@RequestBody List<T> tList) {
        try {
            return Result.success(this.service.saveOrUpdateBatch(tList), ResultCodeEnum.SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(e.getMessage(), false);
        }
    }
}
