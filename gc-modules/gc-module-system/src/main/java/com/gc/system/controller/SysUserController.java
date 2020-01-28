package com.gc.system.controller;

import com.gc.common.auth.model.SysUserPO;
import com.gc.common.auth.service.AuthUserService;
import com.gc.starter.crud.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户controller层
 * @author jackson
 * 2020/1/24 2:22 下午
 */
@RestController
@RequestMapping("sys/user")
public class SysUserController extends BaseController<AuthUserService, SysUserPO> {


}
