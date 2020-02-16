package com.gc.common.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gc.common.auth.core.GcGrantedAuthority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
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
@NoArgsConstructor
public class RestUserDetails implements UserDetails, Serializable {
    private static final long serialVersionUID = -6184955894751051086L;

    @Getter
    private Long userId;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Getter
    private SysUserPO user;

    private Set<GcGrantedAuthority> authorities;

    @Override
    public Collection<GcGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * 获取用户的角色编码
     * @return 用户角色列表
     */
    public Set<String> getRoles() {
        return this.authorities.stream()
                .filter(GcGrantedAuthority::isRole)
                .map(GcGrantedAuthority::getAuthorityValue)
                .collect(Collectors.toSet());
    }

    /**
     * 获取用户的权限列表
     * @return 用户权限列表
     */
    public Set<String> getPermissions() {
        return this.authorities.stream()
                .filter(GcGrantedAuthority :: isPermission)
                .map(GcGrantedAuthority :: getAuthorityValue)
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

    @NotNull
    public static RestUserDetails createByUser(@NotNull SysUserPO user) {
        final RestUserDetails restUserDetails = new RestUserDetails();
        restUserDetails.setUserId(user.getUserId());
        restUserDetails.setUser(user);
        restUserDetails.setUsername(user.getUsername());
        restUserDetails.setPassword(user.getPassword());
        return restUserDetails;
    }
}
