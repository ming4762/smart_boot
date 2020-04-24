package com.gc.auth.security.controller;

import com.gc.auth.security.pojo.dto.UserLoginDTO;
import com.gc.auth.security.service.AuthService;
import com.gc.common.auth.constants.LoginTypeConstants;
import com.gc.common.auth.core.RestUserDetails;
import com.gc.common.auth.model.LoginResult;
import com.gc.common.auth.model.RestUserDetailsImpl;
import com.gc.common.base.message.Result;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * jwt登陆
 * @author jackson
 * 2020/2/14 10:22 下午
 */
@RequestMapping
@ResponseBody
public class LoginController {


    private AuthenticationManager authenticationManager;

    private AuthService authService;


    public LoginController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    /**
     * 登陆接口
     * @param parameter 登录参数
     * @return
     */
    @PostMapping("public/auth/login")
    public Result<LoginResult> login(@RequestBody @Valid UserLoginDTO parameter) {
        return Result.success(this.doLogin(parameter, LoginTypeConstants.WEB));
    }

    /**
     * 移动端登录接口
     * @param parameter 登录参数
     * @return
     */
    @PostMapping("public/auth/mobileLogin")
    public Result<LoginResult> mobileLogin(@RequestBody @Valid UserLoginDTO parameter) {
        return Result.success(this.doLogin(parameter, LoginTypeConstants.MOBILE));
    }

    /**
     * 登出接口
     * @return
     */
    @PostMapping("auth/logout")
    public Result<Object> logout(HttpServletRequest request) {
        this.authService.logout(request);
        return Result.success(null, "登出成功");
    }


    /**
     * 执行登陆
     * @param parameter 登录参数
     * @return
     */
    private LoginResult doLogin(UserLoginDTO parameter, LoginTypeConstants type) {
        final Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(parameter.getUsername(), parameter.getPassword()));

        final RestUserDetails userDetails = (RestUserDetailsImpl) authentication.getPrincipal();
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        final String jwt = this.authService.doLogin(authentication, parameter.getRemember(), type);
        return LoginResult.builder()
                .user(userDetails)
                .token(jwt)
                .roles(userDetails.getRoles())
                .permissions(userDetails.getPermissions())
                .build();
    }

}
