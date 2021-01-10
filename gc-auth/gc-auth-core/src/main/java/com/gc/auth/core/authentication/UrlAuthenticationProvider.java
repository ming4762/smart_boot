package com.gc.auth.core.authentication;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * URl验证接口
 * @author ShiZhongMing
 * 2021/1/4 16:59
 * @since 1.0
 */
public interface UrlAuthenticationProvider extends BeanNameAware {

    /**
     * 是否拥有权限
     * @param request request
     * @param authentication authentication
     * @return 是否拥有权限
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);

    /**
     * 获取bean 名称
     * @return BeanName
     */
    @NonNull
    String getBeanName();
}
