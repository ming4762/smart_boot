package com.gc.auth.security.controller;

import com.gc.common.auth.annotation.NonUrlCheck;
import com.gc.common.auth.core.RestUserDetails;
import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.message.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shizhongming
 * 2020/4/24 9:17 上午
 */
@RequestMapping
@RestController
public class AuthController {

    /**
     * 验证用户是否登录
     * @return 是否登录
     */
    @PostMapping("auth/isLogin")
    @NonUrlCheck
    public Result<Boolean> isLogin() {
        RestUserDetails restUserDetails = AuthUtils.getCurrentUser();
        return Result.success(ObjectUtils.isNotEmpty(restUserDetails));
    }
}
