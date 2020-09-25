package com.gc.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.auth.model.AuthUser;
import com.gc.common.auth.model.Permission;
import com.gc.common.auth.service.AuthUserService;
import com.gc.system.model.SysRolePO;
import com.gc.system.model.SysUserPO;
import com.gc.system.service.SysUserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2020/9/25 17:05
 * @since 1.0
 */
@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final SysUserService sysUserService;

    public AuthUserServiceImpl(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }


    @Override
    public AuthUser getByUsername(@NonNull String username) {
        final SysUserPO user = this.sysUserService.getOne(
                new QueryWrapper<SysUserPO>().lambda()
                .eq(SysUserPO :: getUsername, username)
        );
        return AuthUser.builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .realname(user.getRealname())
                .build();
    }

    @Override
    public Set<String> listRoleCode(@NonNull AuthUser authUser) {
        return this.sysUserService.listRole(authUser.getUserId()).stream()
                .map(SysRolePO::getRoleCode)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Permission> listPermission(@NonNull AuthUser authUser) {
        return null;
    }
}
