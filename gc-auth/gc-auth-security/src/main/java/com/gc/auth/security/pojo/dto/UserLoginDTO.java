package com.gc.auth.security.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户登录dto
 * @author jackson
 * 2020/2/14 10:25 下午
 */
@Getter
@Setter
@ToString
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID = -5567960808566060208L;


    private String username;

    private String password;

    private Boolean remember = false;
}
