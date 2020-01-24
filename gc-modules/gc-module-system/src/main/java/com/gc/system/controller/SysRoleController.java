package com.gc.system.controller;

import com.gc.starter.crud.controller.BaseController;
import com.gc.system.model.SysRolePO;
import com.gc.system.service.SysRoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色业务层
 * @author jackson
 * 2020/1/24 2:21 下午
 */
@RestController
@RequestMapping("sys/role")
public class SysRoleController extends BaseController<SysRoleService, SysRolePO> {
}
