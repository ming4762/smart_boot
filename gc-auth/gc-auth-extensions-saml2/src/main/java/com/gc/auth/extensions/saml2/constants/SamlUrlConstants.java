package com.gc.auth.extensions.saml2.constants;

import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2021/2/26 16:17
 * @since 1.0
 */
public enum SamlUrlConstants {
    /**
     * 通用地址
     */
    COMMON("/saml"),
    /**
     * 登录地址
     */
    LOGIN("/saml/login"),
    LOGOUT("/saml/logout"),
    DISCOVERY("/saml/discovery"),
    SSO("/saml/SSO")
    ;

    @Getter
    private final String url;

    SamlUrlConstants(String url) {
        this.url = url;
    }
}
