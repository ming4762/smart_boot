package com.gc.auth.security.handler;

import com.gc.common.auth.model.RestUserDetails;
import com.gc.common.auth.properties.AuthProperties;
import com.gc.common.base.message.Result;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 登录成功执行器
 * @author shizhongming
 * 2020/1/16 9:04 下午
 */
@Component
public class RestAuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final String LOGIN_TYPE = "type";

    private static final String MOBILE_LOGIN_TYPE = "mobile";

    /**
     * 认证参数
     */
    @Autowired
    private AuthProperties authProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        this.setSessionMaxInactiveInterval(httpServletRequest);
        RestJsonWriter.writeJson(response, this.successData(authentication, httpServletRequest));
    }

    /**
     * 获取返回信息
     * @param authentication
     * @param httpServletRequest
     * @return
     */
    private Result<Map<String, Object>> successData(Authentication authentication, HttpServletRequest httpServletRequest) {
        final RestUserDetails userDetails = (RestUserDetails) authentication.getPrincipal();
        // 处理用户权限信息
        final ImmutableMap<String, Object> result = ImmutableMap.of(
                "user", userDetails,
                "token", httpServletRequest.getSession().getId(),
                "roles", userDetails.getRoles(),
                "permissions", userDetails.getPermissions()
        );
        return Result.success(result);
    }

    /**
     * 设置session过期时间
     * @param request 请求信息
     */
    private void setSessionMaxInactiveInterval(HttpServletRequest request) {
        Long global = this.authProperties.getSession().getTimeout().getGlobal();
        if (StringUtils.equals(MOBILE_LOGIN_TYPE, (String)request.getAttribute(LOGIN_TYPE))) {
            global = this.authProperties.getSession().getTimeout().getMobile();
        }
        request.getSession().setMaxInactiveInterval(global.intValue());
    }

}
