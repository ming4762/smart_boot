package com.gc.auth.core.handler;

import com.gc.auth.core.constants.LoginTypeConstants;
import com.gc.auth.core.data.RestUserDetails;
import com.gc.auth.core.model.LoginParameter;
import com.gc.auth.core.model.LoginResult;
import com.gc.auth.core.model.Permission;
import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.core.utils.RestJsonWriter;
import com.gc.common.base.message.Result;
import com.google.common.collect.Sets;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 登录成功执行器
 * @author shizhongming
 * 2021/1/1 3:49 上午
 */
public class AuthLoginSuccessHandler implements AuthenticationSuccessHandler {


    private final AuthProperties authProperties;

    public AuthLoginSuccessHandler(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        this.setSessionMaxInactiveInterval(httpServletRequest);
        RestJsonWriter.writeJson(httpServletResponse, this.successData(authentication, httpServletRequest));
    }

    /**
     * 获取返回信息
     * @param authentication 令牌
     * @param httpServletRequest request
     * @return 登录信息
     */
    private Result<LoginResult> successData(Authentication authentication, HttpServletRequest httpServletRequest) {
        final RestUserDetails userDetails = (RestUserDetails) authentication.getPrincipal();
        // 处理用户权限信息
        final LoginResult loginResult = LoginResult.builder()
                .user(userDetails)
                .token(userDetails.getToken())
                .roles(userDetails.getRoles())
                .permissions(
                        Optional.ofNullable(userDetails.getPermissions())
                                .map(item -> item.stream().map(Permission::getAuthority).collect(Collectors.toSet()))
                                .orElse(Sets.newHashSet())
                ).build();
        return Result.success(loginResult);
    }

    /**
     * 设置session过期时间
     * @param request 请求信息
     */
    private void setSessionMaxInactiveInterval(HttpServletRequest request) {
        final LoginParameter loginParameter = LoginParameter.create(request);

        // 获取有效期
        Long timeout = authProperties.getSession().getTimeout().getGlobal();
        if (Objects.equals(loginParameter.getLoginType(), LoginTypeConstants.MOBILE)) {
            timeout = authProperties.getSession().getTimeout().getMobile();
        } else if (Objects.equals(loginParameter.getLoginType(), LoginTypeConstants.REMEMBER)) {
            timeout = authProperties.getSession().getTimeout().getRemember();
        }
        request.getSession().setMaxInactiveInterval(timeout.intValue());
    }
}
