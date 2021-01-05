package com.gc.auth.core.constants;

import lombok.Getter;

/**
 * @author shizhongming
 * 2020/9/13 5:05 下午
 */
public enum RoleConstants {
    /**
     * 超级管理员账号
     */
    ROLE_SUPERADMIN("SUPERADMIN");

    @Getter
    private final String role;

    RoleConstants(String role) {
        this.role = role;
    }
}
