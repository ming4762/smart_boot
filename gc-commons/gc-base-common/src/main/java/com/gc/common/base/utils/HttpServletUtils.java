package com.gc.common.base.utils;

import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * HttpServlet 工具类
 * @author shizhongming
 * 2020/6/24 4:06 下午
 */
public class HttpServletUtils {

    private HttpServletUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final int PORT_80 = 80;

    /**
     * 获取request对象
     */
    @Nullable
    public static HttpServletRequest getRequest() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    /**
     * 获取Response对象
     * @return Response对象
     */
    public static HttpServletResponse getResponse() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }

    /**
     * 获取基础路径
     * @return 基础路径
     */
    public static String getBaseServerPath() {
        HttpServletRequest request = getRequest();
        if (Objects.isNull(request)) {
            return null;
        }
        StringBuilder url = new StringBuilder(request.getScheme())
                .append("://")
                .append(request.getServerName());
        // 获取端口号
        int port = request.getServerPort();
        if (port != PORT_80) {
            url.append(":")
                    .append(request.getServerPort());
        }
        return url.toString();
    }
}
