package com.gc.auth.core.data;

import com.gc.auth.core.constants.GrantedAuthorityType;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author jackson
 * 2020/1/29 9:07 下午
 */
public interface GcGrantedAuthority extends GrantedAuthority {

    /**
     * 是否是角色
     * @return
     */
    boolean isRole();

    /**
     * 是否是权限
     * @return
     */
    boolean isPermission();

    /**
     * 获取权限类型
     * @return
     */
    GrantedAuthorityType getType();

    /**
     * 获取权限的值
     * @return
     */
    String getAuthorityValue();
}
