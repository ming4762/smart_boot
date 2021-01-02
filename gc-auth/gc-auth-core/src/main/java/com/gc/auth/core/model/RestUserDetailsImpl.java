package com.gc.auth.core.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gc.auth.core.data.GcGrantedAuthority;
import com.gc.auth.core.data.PermissionGrantedAuthority;
import com.gc.auth.core.data.RestUserDetails;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/16 9:11 下午
 */
@Setter
@NoArgsConstructor
public class RestUserDetailsImpl implements RestUserDetails, Serializable {
    private static final long serialVersionUID = -6184955894751051086L;

    @Getter
    private Long userId;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Getter
    private String realName;

    @Getter
    private String token;

    private Set<GcGrantedAuthority> authorities;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Collection<GcGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * 获取用户的角色编码
     * @return 用户角色列表
     */
    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Set<String> getRoles() {
        if (Objects.isNull(this.authorities)) {
            return Sets.newHashSet();
        }
        return this.authorities.stream()
                .filter(GcGrantedAuthority::isRole)
                .map(GcGrantedAuthority::getAuthorityValue)
                .collect(Collectors.toSet());
    }

    /**
     * 获取用户的权限列表
     * @return 用户权限列表
     */
    @Override
    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Set<Permission> getPermissions() {
        if (Objects.isNull(this.authorities)) {
            return Sets.newHashSet();
        }
        return this.authorities.stream()
                .filter(GcGrantedAuthority :: isPermission)
                .map(item -> ((PermissionGrantedAuthority)item).getPermission())
                .collect(Collectors.toSet());
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getToken() {
        return this.token;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isEnabled() {
        return true;
    }

}
