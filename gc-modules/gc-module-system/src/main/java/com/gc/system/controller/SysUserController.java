package com.gc.system.controller;

import com.gc.common.auth.model.SysUserPO;
import com.gc.common.auth.service.AuthUserService;
import com.gc.common.base.http.HttpStatus;
import com.gc.common.base.message.Result;
import com.gc.starter.crud.controller.BaseController;
import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.log.annotation.Log;
import com.gc.starter.log.constants.LogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户controller层
 * @author jackson
 * 2020/1/24 2:22 下午
 */
@RestController
@RequestMapping("sys/user")
@Api(value = "用户管理", tags = "系统模块")
public class SysUserController extends BaseController<AuthUserService, SysUserPO> {

    /**
     * 添加保存方法
     * @param model 用户实体
     * @return 是否保存成功
     */
    @Override
    @PostMapping("saveUpdate")
    @ApiOperation("添加/更新用户")
    @Log(value = "添加/更新用户", type = LogType.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'save') or hasPermission('sys:user', 'update')")
    protected Result<Boolean> saveUpdate(@RequestBody SysUserPO model) {
        return super.saveUpdate(model);
    }

    @Override
    @PostMapping("save")
    @ApiOperation("添加用户")
    @Log(value = "添加用户", type = LogType.ADD)
    @PreAuthorize("hasPermission('sys:user', 'save')")
    protected Result<Boolean> save(@RequestBody SysUserPO model) {
        return super.save(model);
    }

    @Override
    @PostMapping("update")
    @ApiOperation("更新用户")
    @Log(value = "更新用户", type = LogType.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'update')")
    protected Result<Boolean> update(@RequestBody SysUserPO model) {
        return super.update(model);
    }

    @Override
    @PostMapping("list")
    @ApiOperation(value = "查询用户列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:user', 'query')")
    protected Result<Object> list(@RequestBody PageQueryParameter<String, Object> parameter) {
        return super.list(parameter);
    }

    /**
     * 通过ID批量删除
     * @param idList ID集合
     * @return 是否删除成功
     */
    @PreAuthorize("hasPermission('sys:user', 'delete')")
    @ApiOperation(value = "通过ID批量删除用户")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除用户", type = LogType.DELETE)
    public Result<Boolean> batchDeleteByid(@RequestBody List<Long> idList) {
        if (idList.isEmpty()) {
            return Result.ofStatus(HttpStatus.PARAM_NOT_NULL, "用户ID集合不能为空");
        }
        return Result.success(this.service.removeByIds(idList));
    }
}
