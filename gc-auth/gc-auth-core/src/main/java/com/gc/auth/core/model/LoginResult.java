package com.gc.auth.core.model;

import com.gc.auth.core.data.RestUserDetails;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

/**
 * 登录结果
 * @author ShiZhongMing
 * 2020/12/30 15:56
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult implements Serializable {

    private static final long serialVersionUID = -5416368660527838625L;

    private RestUserDetails user;

    private String token;

    private Set<String> roles;

    private Set<String> permissions;
}
