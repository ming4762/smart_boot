package com.gc.common.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/16 9:11 下午
 */
@Setter
public class RestUserDetails implements UserDetails, Serializable {
    private static final long serialVersionUID = -6184955894751051086L;

    private static final String ROLE_START = "ROLE_";

    @Getter
    private Long userId;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Getter
    private SysUserPO user;

    private Set<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * 获取用户的角色编码
     * @return
     */
    public Set<String> getRoles() {
        return this.authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(item -> item.startsWith(ROLE_START))
                .map(item -> item.replace(ROLE_START, ""))
                .collect(Collectors.toSet());
    }

    /**
     * 获取用户的权限列表
     * @return
     */
    public Set<String> getPermissions() {
        return this.authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(item -> !item.startsWith(ROLE_START))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
