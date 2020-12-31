package com.gc.auth.core.matcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义matcher，同时校验扩展名、路径
 * @author ShiZhongMing
 * 2020/11/18 10:05
 * @since 1.0
 */
@Slf4j
public class ExtensionPathMatcher implements RequestMatcher {

    /**
     * 文件前缀开头
     */
    private static final String PREFIX_SUFFIX = "*.";

    private final String pattern;
    private final HttpMethod httpMethod;

    public ExtensionPathMatcher(HttpMethod httpMethod, String pattern) {
        this.httpMethod = httpMethod;
        this.pattern = pattern;
    }

    public ExtensionPathMatcher(String pattern) {
        this.pattern = pattern;
        this.httpMethod = null;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        // 验证请求方式
        if (this.httpMethod != null && org.springframework.util.StringUtils.hasText(request.getMethod()) && this.httpMethod != valueOf(request.getMethod())) {
            if (log.isDebugEnabled()) {
                log.debug("Request '" + request.getMethod() + " " + request.getRequestURI() + "' doesn't match '" + this.httpMethod + " " + this.pattern + "'");
            }
            return false;
        }
        // 判断是否是文件扩展名
        if (StringUtils.startsWith(this.pattern, PREFIX_SUFFIX)) {
            // 判断文件结尾
            if (StringUtils.endsWith(request.getRequestURI(),this.pattern.substring(1))) {
                return true;
            }
        }
        // 验证路径
        AntPathRequestMatcher matcher = new AntPathRequestMatcher(this.pattern);
        return matcher.matches(request);
    }


    private static HttpMethod valueOf(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

}
