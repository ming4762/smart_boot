package com.gc.auth.security.authentication;

import com.gc.auth.security.constants.RoleConstants;
import com.gc.common.auth.annotation.NonUrlCheck;
import com.gc.common.auth.core.RestUserDetails;
import com.gc.common.auth.exception.AuthException;
import com.gc.common.auth.model.Permission;
import com.gc.common.auth.properties.AuthProperties;
import com.gc.common.base.http.HttpStatus;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 提供动态URL校验
 * @author shizhongming
 * 2020/4/29 9:10 上午
 */
public class DynamicUrlCheckProvider {

    private final RequestMappingHandlerMapping mapping;

    private final AuthProperties authProperties;

    /**
     * 所有URL映射
     */
    private Multimap<String, UrlMapping> allUrlMapping = null;

    public DynamicUrlCheckProvider(RequestMappingHandlerMapping mapping, AuthProperties authProperties) {
        this.mapping = mapping;
        this.authProperties = authProperties;
    }

    /**
     * 校验是否拥有URL权限
     * @param request 请求信息
     * @param authentication 用户认证信息
     * @return 是否认证通过
     */
    public boolean hasUrlPermission(HttpServletRequest request, Authentication authentication) {
        // 判断是否需要校验URL
        boolean check = this.hasCheck(request);
        if (!check) {
            return true;
        }
        boolean hasPermission = false;
        Object userInfo = authentication.getPrincipal();
        if (userInfo instanceof RestUserDetails) {
            RestUserDetails restUserDetails = (RestUserDetails) userInfo;
            if (restUserDetails.getRoles().contains(RoleConstants.ROLE_SUPERADMIN.getRole())) {
                return true;
            }
            Set<Permission> permissionList = restUserDetails.getPermissions();
            if (ObjectUtils.isNotEmpty(permissionList)) {
                for (Permission permission : permissionList) {
                    AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(permission.getUrl(), permission.getMethod().name());
                    if (antPathMatcher.matches(request)) {
                        hasPermission = true;
                        break;
                    }
                }
            }
        }

        return hasPermission;
    }

    /**
     * 判断URL是否需要校验
     * @param request 请求体
     * @return 改URL是否需要校验
     */
    private boolean hasCheck(HttpServletRequest request) {
        // 判断是否需要校验地址
//        if (this.isIgnore(request)) {
//            return false;
//        }
        if (Objects.isNull(this.allUrlMapping)) {
            this.allUrlMapping = this.getAllUrlMapping();
        }
        boolean check = true;
        boolean match = false;
        // 获取请求方法
        String currentMethod = request.getMethod();
        for (String uri : this.allUrlMapping.keySet()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(uri);
            if (antPathMatcher.matches(request)) {
                match = true;
                Collection<UrlMapping> urlMappingList = this.allUrlMapping.get(uri);
                // 获取对应的请求
                List<UrlMapping> filterUrlMapping = urlMappingList.stream()
                        .filter(item -> StringUtils.equals(currentMethod, item.getRequestMethod().name()))
                        .collect(Collectors.toList());
                if (filterUrlMapping.isEmpty()) {
                    throw new AuthException(HttpStatus.METHOD_NOT_ALLOWED);
                }
                // 判断方法是否需要校验
                UrlMapping urlMapping = filterUrlMapping.get(0);
                NonUrlCheck nonUrlCheck = AnnotationUtils.findAnnotation(urlMapping.getHandlerMethod().getMethod(), NonUrlCheck.class);
                if (Objects.isNull(nonUrlCheck)) {
                    nonUrlCheck = AnnotationUtils.findAnnotation(urlMapping.getHandlerMethod().getBeanType(), NonUrlCheck.class);
                }
                check = Objects.isNull(nonUrlCheck);
                break;
            }
        }
        if (!match) {
            return false;
        }
        return check;
    }

    /**
     * 判断是否是忽略地址
     * @param request 请求体
     * @return 是否忽略
     */
    private boolean isIgnore(HttpServletRequest request) {
        // 判断pattern
        final AuthProperties.IgnoreConfig ignores = this.authProperties.getIgnores();
        for (String url : ignores.getPattern()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url);
            if (antPathMatcher.matches(request)) {
                return true;
            }
        }
        for (String url : ignores.getPost()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url, HttpMethod.POST.name());
            if (antPathMatcher.matches(request)) {
                return true;
            }
        }

        for (String url : ignores.getDelete()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url, HttpMethod.DELETE.name());
            if (antPathMatcher.matches(request)) {
                return true;
            }
        }

        for (String url : ignores.getPut()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url, HttpMethod.PUT.name());
            if (antPathMatcher.matches(request)) {
                return true;
            }
        }

        for (String url : ignores.getHead()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url, HttpMethod.HEAD.name());
            if (antPathMatcher.matches(request)) {
                return true;
            }
        }

        for (String url : ignores.getPatch()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url, HttpMethod.PATCH.name());
            if (antPathMatcher.matches(request)) {
                return true;
            }
        }

        for (String url : ignores.getOptions()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url, HttpMethod.OPTIONS.name());
            if (antPathMatcher.matches(request)) {
                return true;
            }
        }

        for (String url : ignores.getTrace()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url, HttpMethod.TRACE.name());
            if (antPathMatcher.matches(request)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 获取url映射
     * @return URL映射
     */
    @NonNull
    private Multimap<String, UrlMapping> getAllUrlMapping() {
        Multimap<String, UrlMapping> urlMappings = ArrayListMultimap.create();

        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
            // 获取当前 key 下的获取所有URL
            Set<String> urls = requestMappingInfo.getPatternsCondition()
                    .getPatterns();
            RequestMethodsRequestCondition method = requestMappingInfo.getMethodsCondition();
            urls.forEach(s -> {
                List<UrlMapping> urlMappingList = method.getMethods().stream()
                        .map(requestMethod -> {
                            UrlMapping urlMapping = new UrlMapping();
                            urlMapping.setRequestMethod(requestMethod);
                            urlMapping.setHandlerMethod(handlerMethod);
                            return urlMapping;
                        }).collect(Collectors.toList());
                urlMappings.putAll(s, urlMappingList);
            });

        });
        return urlMappings;
    }

    /**
     * URL映射
     */
    @Getter
    @Setter
    static
    class UrlMapping {
        /**
         * 请求方法
         */
        private RequestMethod requestMethod;

        /**
         * 执行目标方法
         */
        private HandlerMethod handlerMethod;
    }
}
