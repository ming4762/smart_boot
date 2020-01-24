package com.gc.auth.security.handler;

import com.gc.auth.security.model.RestUserDetails;
import com.gc.common.base.message.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功执行器
 * @author shizhongming
 * 2020/1/16 9:04 下午
 */
@Component
public class RestAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        RestUserDetails userDetails = (RestUserDetails) authentication.getPrincipal();
        httpServletRequest.getSession().setAttribute("user", userDetails);
        RestJsonWriter.writeJson(response, Result.success(userDetails));
    }
}
