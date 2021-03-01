package com.gc.auth.core.handler;

import com.gc.auth.core.model.LoginResult;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/3/1 9:56
 * @since 1.0
 */
public interface AuthSuccessDataHandler {

    /**
     * 登录成功数据
     * @param authentication 认证信息
     * @param request 请求体
     * @return 登录成功数据
     */
    LoginResult successData(Authentication authentication, HttpServletRequest request);
}
