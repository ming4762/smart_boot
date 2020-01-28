package com.gc.system.service;

import com.gc.common.auth.service.AuthUserService;
import com.gc.system.model.SysRolePO;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author jackson
 * 2020/1/24 3:49 下午
 */
public interface SysUserService extends AuthUserService {

    /**
     * 查询用户的角色
     * @param userId 用户Id
     * @return 角色列表
     */
    @NotNull
    List<SysRolePO> listRole(@NotNull Long userId);
}
