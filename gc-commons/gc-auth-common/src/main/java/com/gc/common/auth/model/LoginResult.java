package com.gc.common.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * 登陆结果
 * @author jackson
 * 2020/2/15 9:41 上午
 */
@Getter
@Setter
@Builder
public class LoginResult implements Serializable {
    private static final long serialVersionUID = -5416368660527838625L;

    private RestUserDetails user;

    private String token;

    private Set<String> roles;

    private Set<String> permissions;
}
