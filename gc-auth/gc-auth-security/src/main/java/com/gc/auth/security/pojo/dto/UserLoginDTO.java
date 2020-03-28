package com.gc.auth.security.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

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

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;

    private Boolean remember;

    public boolean getRemember() {
        if (Objects.isNull(remember)) {
            remember = false;
        }
        return remember;
    }
}
