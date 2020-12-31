package com.gc.common.base.utils;

import com.gc.common.base.exception.IllegalAccessRuntimeException;
import com.gc.common.base.exception.InstantiationRuntimeException;
import com.gc.common.base.exception.InvocationTargetRuntimeException;
import com.gc.common.base.exception.NoSuchMethodRuntimeException;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
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
     * 从请求参数中创建实体
     * TODO: 开发中
     * @param request 请求
     * @param clazz 类型
     * @param <T> 类型
     * @return 数据
     */
    public static <T> T getDataFromRequestParameter(ServletRequest request, Class<T> clazz) {
        try {
            T result = clazz.getConstructor().newInstance();
            final Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                // 获取参数值
                String value = request.getParameter(field.getName());
                if (Objects.isNull(value)) {
                    continue;
                }
            }
            return result;
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(e);
        } catch (InstantiationException e) {
            throw new InstantiationRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(e);
        }
    }

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
