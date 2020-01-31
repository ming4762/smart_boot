package com.gc.system.controller;

import com.gc.common.auth.model.SysUserPO;
import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.message.Result;
import com.gc.starter.crud.controller.BaseController;
import com.gc.system.model.SysUserGroupPO;
import com.gc.system.pojo.dto.UserGroupUserSaveDTO;
import com.gc.system.pojo.dto.UserUserGroupSaveDTO;
import com.gc.system.service.SysUserGroupService;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户组层
 * @author jackson
 * 2020/1/24 3:10 下午
 */
@RestController
@RequestMapping("sys/userGroup")
@Api(value = "用户组管理", tags = "系统模块")
public class SysUserGroupController extends BaseController<SysUserGroupService, SysUserGroupPO> {

    @ApiOperation(value = "添加修改用户组")
    @PostMapping("saveUpdate")
    @PreAuthorize("hasPermission('sys:userGroup', 'save') or hasPermission('sys:userGroup', 'update')")
    protected Result<Boolean> saveUpdate(@RequestBody @Valid SysUserGroupPO model, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return Result.failure(result);
        }
        return Result.success(this.service.saveOrUpdateWithAllUser(model, AuthUtils.getCurrentUserId()));
    }

    /**
     * 执行删除
     * @param idList
     * @return
     */
    @PreAuthorize("hasPermission('sys:userGroup', 'delete')")
    @ApiOperation(value = "通过ID批量删除用户组")
    @PostMapping("batchDeleteById")
    public Result<Boolean> batchDeleteById(@RequestBody List<Long> idList) {
        return Result.success(this.service.removeByIds(idList));
    }

    @Override
    @PostMapping("list")
    @ApiOperation(value = "查询用户组列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:userGroup', 'query')")
    protected Result<Object> list(@RequestBody Map<String, Object> parameter) {
        return super.list(parameter);
    }

    /**
     * 通过ID查询用户信息
     * @param id
     * @return
     */
    @RequestMapping("listUserById")
    @ApiOperation(value = "通过用户组ID查询用户列表", httpMethod = "POST")
    @PreAuthorize("hasPermission('sys:userGroup', 'query')")
    public Result<List<SysUserPO>> listUserById(@RequestBody Long id) {
        final Map<Long, List<SysUserPO>> result = this.service.listUserByIds(ImmutableList.of(id));
        if (result.containsKey(id)) {
            return Result.success(result.get(id));
        }
        return Result.success(ImmutableList.of());
    }

    /**
     * 通过ID查询用户ID集合
     * @param id
     * @return
     */
    @PostMapping(value = "listUserIdById")
    @ApiOperation(value = "通过用户组ID查询用户ID列表")
    @PreAuthorize("hasPermission('sys:userGroup', 'query')")
    public Result<List<Long>> listUserIdById(@RequestBody Long id) {
        final Map<Long, List<Long>> result = this.service.listUserIdByIds(ImmutableList.of(id));
        if (result.containsKey(id)) {
            return Result.success(result.get(id));
        }
        return Result.success(ImmutableList.of());
    }

    /**
     * 保存用户
     * @param parameter
     * @param result
     * @return
     */
    @PostMapping("saveUserGroupByGroupId")
    @PreAuthorize("hasPermission('sys:userGroup', 'saveUser')")
    @ApiOperation(value = "设置用户组包含的用户")
    public Result<Boolean> saveUserGroupByGroupId(@RequestBody @Valid UserGroupUserSaveDTO parameter, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result);
        }
        return Result.success(this.service.saveUserGroupByGroupId(parameter));
    }

    /**
     * 保存用户
     * @param parameter
     * @param result
     * @return
     */
    @PostMapping("saveUserGroupByUserId")
    @ApiOperation(value = "设置用户所属用户组")
    @PreAuthorize("hasPermission('sys:userGroup', 'saveUser')")
    public Result<Boolean> saveUserGroupByUserId(@RequestBody @Valid UserUserGroupSaveDTO parameter, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result);
        }
        return Result.success(this.service.saveUserGroupByUserId(parameter));
    }
}
