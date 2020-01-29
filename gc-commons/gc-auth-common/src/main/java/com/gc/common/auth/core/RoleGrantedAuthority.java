package com.gc.common.auth.core;

import com.gc.common.auth.constants.GrantedAuthorityType;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * @author jackson
 * 2020/1/29 9:04 下午
 */
@AllArgsConstructor
public class RoleGrantedAuthority implements GcGrantedAuthority {
    private static final long serialVersionUID = 4316900997007482876L;

    private static final String ROLE_START = "ROLE_";

    private String roleCode;

    @Override
    @NotNull
    public String getAuthority() {
        return ROLE_START + this.roleCode;
    }

    @Override
    public boolean isRole() {
        return true;
    }

    @Override
    public boolean isPermission() {
        return false;
    }

    @Override
    public GrantedAuthorityType getType() {
        return GrantedAuthorityType.ROLE;
    }

    @Override
    public String getAuthorityValue() {
        return this.roleCode;
    }
}
