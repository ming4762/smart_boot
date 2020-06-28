package com.gc.system.controller;

import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.message.Result;
import com.gc.starter.crud.controller.BaseController;
import com.gc.system.model.SysRolePO;
import com.gc.system.service.SysRoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色业务层
 * @author shizhongming
 * 2020/1/24 2:21 下午
 */
@RestController
@RequestMapping("sys/role")
public class SysRoleController extends BaseController<SysRoleService, SysRolePO> {

    @Override
    @PostMapping("save")
    public Result<Boolean> save(@RequestBody SysRolePO model) {
        return Result.success(this.service.saveWithUser(model, AuthUtils.getCurrentUserId()));
    }
}
