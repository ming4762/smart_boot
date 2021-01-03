package com.gc.auth.core.handler;

import com.gc.auth.core.utils.RestJsonWriter;
import com.gc.common.base.message.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功执行器
 * @author shizhongming
 * 2020/1/17 7:41 下午
 */
public class AuthLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        RestJsonWriter.writeJson(httpServletResponse, Result.success(null, "登出成功"));
    }
}
