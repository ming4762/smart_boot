package com.gc.auth.core.data;


import com.gc.auth.core.constants.GrantedAuthorityType;
import com.gc.auth.core.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jackson
 * 2020/1/29 9:06 下午
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionGrantedAuthority implements GcGrantedAuthority {

    private static final long serialVersionUID = -8344886266135685662L;

    private Permission permission;

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
        return this.permission.getAuthority();
    }

    @Override
    public String getAuthority() {
        return this.permission.getAuthority();
    }
}
