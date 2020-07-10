package com.gc.common.auth.core;

import com.gc.common.auth.model.Permission;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * @author jackson
 * 2020/4/13 9:32 上午
 */
public interface RestUserDetails extends UserDetails {

    /**
     * 获取用户ID
     * @return 用户ID
     */
    Long getUserId();

    /**
     * 获取用户姓名
     * @return 姓名
     */
    String getRealname();

    /**
     * 获取角色
     * @return 角色编码列表
     */
    @NonNull
    Set<String> getRoles();

    /**
     * 获取权限
     * @return 权限编码列表
     */
    @NonNull
    Set<Permission> getPermissions();
}
