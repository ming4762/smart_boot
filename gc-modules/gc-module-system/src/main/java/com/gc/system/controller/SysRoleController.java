package com.gc.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.auth.annotation.NonUrlCheck;
import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.message.Result;
import com.gc.starter.crud.controller.BaseController;
import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.log.annotation.Log;
import com.gc.starter.log.constants.LogType;
import com.gc.system.model.SysRoleFunctionPO;
import com.gc.system.model.SysRolePO;
import com.gc.system.pojo.dto.role.RoleMenuSaveDTO;
import com.gc.system.service.SysRoleFunctionService;
import com.gc.system.service.SysRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色业务层
 * @author shizhongming
 * 2020/1/24 2:21 下午
 */
@RestController
@RequestMapping("sys/role")
@NonUrlCheck
public class SysRoleController extends BaseController<SysRoleService, SysRolePO> {

    private final SysRoleFunctionService sysRoleFunctionService;

    public SysRoleController(SysRoleFunctionService sysRoleFunctionService) {
        this.sysRoleFunctionService = sysRoleFunctionService;
    }

    @Override
    @PostMapping("save")
    @ApiOperation(value = "添加角色")
    @Log(value = "添加角色", type = LogType.ADD)
    @PreAuthorize("hasPermission('sys:role', 'save')")
    public Result<Boolean> save(@RequestBody SysRolePO model) {
        return Result.success(this.service.saveWithUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("update")
    @ApiOperation(value = "更新角色")
    @Log(value = "更新角色", type = LogType.UPDATE)
    @PreAuthorize("hasPermission('sys:role', 'update')")
    public Result<Boolean> update(@RequestBody SysRolePO model) {
        return super.update(model);
    }

    @Override
    @PostMapping("list")
    @ApiOperation(value = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody PageQueryParameter<String, Object> parameter) {
        return super.list(parameter);
    }

    @Override
    @ApiOperation(value = "添加修改角色")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改角色", type = LogType.UPDATE)
    @PreAuthorize("hasPermission('sys:role', 'save') or hasPermission('sys:role', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody SysRolePO model) {
        return super.saveUpdate(model);
    }

    @Override
    @PreAuthorize("hasPermission('sys:role', 'delete')")
    @ApiOperation(value = "通过ID批量删除角色")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除角色", type = LogType.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    @Override
    @ApiOperation(value = "通过ID查询")
    @PostMapping("getById")
    public Result<SysRolePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 获取角色对应的功能ID集合
     * @param roleId 角色ID
     * @return 功能ID集合
     */
    @ApiOperation(value = "获取角色对应的功能ID集合")
    @PostMapping("listFunctionId")
    public Result<List<Long>> listFunctionId(@RequestBody Long roleId) {
        return Result.success(
                this.sysRoleFunctionService.list(
                        new QueryWrapper<SysRoleFunctionPO>().lambda()
                        .select(SysRoleFunctionPO :: getFunctionId)
                        .eq(SysRoleFunctionPO :: getRoleId, roleId)
                ).stream().map(SysRoleFunctionPO :: getFunctionId)
                .collect(Collectors.toList())
        );
    }


    @ApiOperation(value = "保存角色功能")
    @PostMapping("saveRoleMenu")
    @Log(value = "保存角色功能", type = LogType.UPDATE)
    @PreAuthorize("hasPermission('sys:role', 'update')")
    public Result<Boolean> saveRoleMenu(@RequestBody @Valid RoleMenuSaveDTO parameter) {
        return Result.success(this.service.saveRoleMenu(parameter));
    }
}
