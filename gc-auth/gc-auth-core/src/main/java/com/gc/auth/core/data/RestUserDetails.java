package com.gc.auth.core.data;

import com.gc.auth.core.model.Permission;
import lombok.NonNull;
import org.springframework.lang.Nullable;
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
    String getRealName();

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

    /**
     * 设置token
     * @param token token
     */
    void setToken(String token);

    /**
     * 获取token
     * @return token
     */
    String getToken();

    /**
     * 获取区域信息
     * @return 区域信息
     */
    @Nullable
    String getLocale();
}
