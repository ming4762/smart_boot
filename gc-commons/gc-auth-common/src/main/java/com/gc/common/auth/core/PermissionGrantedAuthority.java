package com.gc.common.auth.core;

import com.gc.common.auth.constants.GrantedAuthorityType;
import lombok.AllArgsConstructor;

/**
 * @author jackson
 * 2020/1/29 9:06 下午
 */
@AllArgsConstructor
public class PermissionGrantedAuthority implements GcGrantedAuthority {

    private static final long serialVersionUID = -8344886266135685662L;

    private String permission;

    @Override
    public boolean isRole() {
        return false;
    }

    @Override
    public boolean isPermission() {
        return true;
    }

    @Override
    public GrantedAuthorityType getType() {
        return GrantedAuthorityType.PERMISSION;
    }

    @Override
    public String getAuthorityValue() {
        return permission;
    }

    @Override
    public String getAuthority() {
        return permission;
    }
}
