package com.gc.common.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2020/9/25 16:54
 * @since 1.0
 */
@Getter
@Setter
@Builder
public class AuthUser implements Serializable {

    private static final long serialVersionUID = 1978668286729284813L;
    private Long userId;

    private String realname;

    private String username;

    private String password;
}
