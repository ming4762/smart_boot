package com.gc.auth.security.authentication;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 登录管理
 * @author jackson
 * 2020/1/23 8:17 下午
 */
@Component
public class RestAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final String USERNAME_PASSWORD_NULL = "用户名密码不能为空";

    private static final String USERNAME_PASSWORD_ERROR = "用户名密码错误";

    private static final String NONE_PROVIDED = "NONE_PROVIDED";

    private final UserDetailsService restUserDetailsService;

    public RestAuthenticationProvider(UserDetailsService restUserDetailsService) {
        this.restUserDetailsService = restUserDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {

        if (ObjectUtils.isEmpty(authentication.getCredentials())) {
            logger.debug("登录失败:密码不存在");
            throw new InternalAuthenticationServiceException(USERNAME_PASSWORD_NULL);
        }
        final String password = authentication.getCredentials().toString();
        if (!StringUtils.equals(userDetails.getPassword(), password)) {
            logger.debug("登录失败：密码错误");
            throw new InternalAuthenticationServiceException(USERNAME_PASSWORD_ERROR);
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        String password = (String) authentication.getCredentials();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.equals(NONE_PROVIDED, username)) {
            throw new InternalAuthenticationServiceException(USERNAME_PASSWORD_NULL);
        }
        UserDetails user = this.restUserDetailsService.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new InternalAuthenticationServiceException(USERNAME_PASSWORD_ERROR);
        }
        return user;
    }
}
