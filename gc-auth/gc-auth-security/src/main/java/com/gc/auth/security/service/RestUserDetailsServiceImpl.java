package com.gc.auth.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.auth.core.GcGrantedAuthority;
import com.gc.common.auth.core.PermissionGrantedAuthority;
import com.gc.common.auth.core.RoleGrantedAuthority;
import com.gc.common.auth.model.RestUserDetails;
import com.gc.common.auth.model.SysUserPO;
import com.gc.common.auth.service.AuthUserService;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * @author jackson
 * 2020/1/23 7:34 下午
 */
@Component
public class RestUserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private AuthUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (StringUtils.isEmpty(username)) {
            return null;
        }
        // 查询用户
        final SysUserPO user = this.userService.getOne(
                new QueryWrapper<SysUserPO>().lambda().eq(SysUserPO::getUsername, username)
        );
        if (Objects.isNull(user)) {
            return null;
        }
        // todo：测试数据
        Set<GcGrantedAuthority> grantedAuthoritySet = Sets.newHashSet(
          new RoleGrantedAuthority("SUPERADMIN"),
          new PermissionGrantedAuthority("123")
        );
        // 查询用户角色信息
        final RestUserDetails restUserDetails = new RestUserDetails();
        restUserDetails.setUserId(user.getUserId());
        restUserDetails.setUser(user);
        restUserDetails.setUsername(user.getUsername());
        restUserDetails.setPassword(user.getPassword());
        restUserDetails.setAuthorities(grantedAuthoritySet);
        return restUserDetails;
    }
}
