package com.gc.system.controller;


import com.gc.auth.core.annotation.NonUrlCheck;
import com.gc.auth.core.utils.AuthUtils;
import com.gc.common.base.message.Result;
import com.gc.starter.crud.controller.BaseController;
import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.log.annotation.Log;
import com.gc.starter.log.constants.LogType;
import com.gc.system.model.SysFunctionPO;
import com.gc.system.service.SysFunctionService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 功能controller
 * @author 史仲明
 * 2020/1/27 12:16 下午
 */
@RestController
@RequestMapping("sys/function")
@NonUrlCheck
public class SysFunctionController extends BaseController<SysFunctionService, SysFunctionPO> {


    @Override
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除功能", type = LogType.DELETE)
    @ApiOperation(value = "通过ID批量删除功能", httpMethod = "POST")
    @PreAuthorize("hasPermission('sys:function', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return Result.success(this.service.removeByIds(idList));
    }

    @PostMapping("getById")
    @Override
    @ApiOperation(value = "通过ID获取", httpMethod = "POST")
    public Result<SysFunctionPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @Override
    @PostMapping("saveUpdate")
    @ApiOperation(value = "添加修改功能")
    @Log(value = "添加保存功能", type = LogType.UPDATE)
    @PreAuthorize("hasPermission('sys:function', 'save') or hasPermission('sys:function', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody SysFunctionPO model) {
        return Result.success(this.service.saveOrUpdateWithAllUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("list")
    @ApiOperation(value = "查询功能列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody PageQueryParameter<String, Object> parameter) {
        return super.list(parameter);
    }
}
