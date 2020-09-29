package com.gc.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.auth.annotation.NonUrlCheck;
import com.gc.common.base.http.HttpStatus;
import com.gc.common.base.message.Result;
import com.gc.starter.crud.controller.BaseController;
import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.log.annotation.Log;
import com.gc.starter.log.constants.LogType;
import com.gc.system.model.SysFunctionPO;
import com.gc.system.model.SysUserPO;
import com.gc.system.model.SysUserRolePO;
import com.gc.system.pojo.dto.user.UserSaveDTO;
import com.gc.system.pojo.dto.user.UserSetRoleDTO;
import com.gc.system.service.SysUserRoleService;
import com.gc.system.service.SysUserService;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户controller层
 * @author shizhongming
 * 2020/1/24 2:22 下午
 */
@RestController
@RequestMapping("sys/user")
@Api(value = "用户管理", tags = "系统模块")
@NonUrlCheck
public class SysUserController extends BaseController<SysUserService, SysUserPO> {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 添加保存方法
     * @param parameter 用户实体
     * @return 是否保存成功
     */
    @PostMapping("saveUpdate")
    @ApiOperation("添加/更新用户")
    @Log(value = "添加/更新用户", type = LogType.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'save') or hasPermission('sys:user', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid UserSaveDTO parameter) {
        SysUserPO model = new SysUserPO();
        BeanUtils.copyProperties(parameter, model);
        return Result.success(this.service.saveOrUpdateWithAllUser(model, 1L));
    }

    @Override
    @PostMapping("save")
    @ApiOperation("添加用户")
    @Log(value = "添加用户", type = LogType.ADD)
    @PreAuthorize("hasPermission('sys:user', 'save')")
    public Result<Boolean> save(@RequestBody @Valid SysUserPO model) {
        return super.save(model);
    }

    @Override
    @PostMapping("update")
    @ApiOperation("更新用户")
    @Log(value = "更新用户", type = LogType.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'update')")
    public Result<Boolean> update(@RequestBody SysUserPO model) {
        return super.update(model);
    }

    @Override
    @PostMapping("list")
    @ApiOperation(value = "查询用户列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:user', 'query')")
    public Result<Object> list(@RequestBody PageQueryParameter<String, Object> parameter) {
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
    @Override
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (idList.isEmpty()) {
            return Result.ofStatus(HttpStatus.PARAM_NOT_NULL, "用户ID集合不能为空");
        }
        return Result.success(this.service.removeByIds(idList));
    }

    /**
     * 查询用户菜单信息
     * @return 用户菜单
     */
    @ApiOperation(value = "查询用户菜单信息")
    @PostMapping("listUserMenu")
    public Result<List<SysFunctionPO>> listUserMenu() {
        return Result.success(this.service.listCurrentUserMenu());
    }

    /**
     * 通过用户ID查询角色ID
     * @param userId 用户ID
     * @return 角色ID
     */
    @ApiOperation(value = "查询角色ID列表")
    @PostMapping("listRoleId")
    public Result<Set<Long>> listRoleId(@RequestBody Long userId) {
        if (Objects.isNull(userId)) {
            return Result.success(Sets.newHashSet());
        }
        return Result.success(
                this.sysUserRoleService.list(
                        new QueryWrapper<SysUserRolePO>().lambda()
                        .select(SysUserRolePO :: getRoleId)
                        .eq(SysUserRolePO :: getUserId, userId)
                ).stream().map(SysUserRolePO :: getRoleId)
                .collect(Collectors.toSet())
        );
    }

    @PostMapping("setRole")
    @Log(value = "设置角色", type = LogType.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'setRole')")
    @ApiOperation(value = "设置角色")
    public Result<Boolean> setRole(@RequestBody @Valid UserSetRoleDTO parameter) {
        return Result.success(this.service.setRole(parameter));
    }
}
