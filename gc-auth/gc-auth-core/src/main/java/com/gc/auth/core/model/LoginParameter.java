package com.gc.auth.core.model;

import com.gc.auth.core.constants.LoginTypeConstants;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录参数
 * @author shizhongming
 * 2021/1/1 4:15 上午
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginParameter {

    private String username;

    private String password;

    LoginTypeConstants loginType;

    public static LoginParameter create(@NonNull HttpServletRequest request) {
        String type = request.getParameter("type");
        LoginTypeConstants loginType = LoginTypeConstants.WEB;
        if(StringUtils.isNotBlank(type)) {
            loginType = LoginTypeConstants.valueOf(type);
        }
        return LoginParameter.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .loginType(loginType)
                .build();
    }
}
