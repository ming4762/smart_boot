package com.gc.starter.i18n.resolver;

import com.gc.auth.core.data.RestUserDetails;
import com.gc.auth.core.utils.AuthUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;

/**
 * 优先从请求头获取，如没有请求头，从用户信息获取
 * @author shizhongming
 * 2021/1/23 9:20 下午
 */
public class AcceptHeaderAndUserLocaleResolver extends AcceptHeaderLocaleResolver {


    @Override
    @NonNull
    public Locale resolveLocale(@NonNull HttpServletRequest request) {
        final Locale locale = super.resolveLocale(request);
        if (Objects.nonNull(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
            if (StringUtils.isBlank(locale.getLanguage())) {
                return Locale.forLanguageTag(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
            }
            return locale;
        }
        // 获取用户语言信息
        final RestUserDetails restUserDetails = AuthUtils.getCurrentUser();
        if (Objects.nonNull(restUserDetails) && StringUtils.isNotBlank(restUserDetails.getLocale())) {
            return Locale.forLanguageTag(restUserDetails.getLocale());
        }
        return locale;
    }
}
