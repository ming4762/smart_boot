package com.gc.starter.log.aspect;

import com.gc.common.base.message.Result;
import com.gc.common.base.utils.AopUtils;
import com.gc.common.base.utils.IpUtils;
import com.gc.common.base.utils.JsonUtils;
import com.gc.starter.exception.handler.ExceptionMessageHandler;
import com.gc.starter.log.LogProperties;
import com.gc.starter.log.annotation.Log;
import com.gc.starter.log.constants.LogIdentConstant;
import com.gc.starter.log.handler.LogHandler;
import com.gc.starter.log.model.SysLogPO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日志切面
 * @author shizhongming
 * 2020/1/22 1:54 下午
 */
@Aspect
@Slf4j
public final class LogAspect {

    private static final Set<Class<?>> EXCLUDE_CLASS = Sets.newHashSet(ServletRequest.class);

    /**
     * 保存日志的编码集合
     */
    private List<Integer> codeList;

    @Autowired
    private LogProperties logProperties;

    @Autowired
    private LogHandler logHandler;

    @Autowired
    private ExceptionMessageHandler exceptionMessageHandler;

    @PostConstruct
    public void init() {
        if (this.codeList == null) {
            String codes = this.logProperties.getCodes();
            if (StringUtils.isEmpty(codes)) {
                this.codeList = Lists.newArrayList();
            } else {
                this.codeList = Arrays.stream(codes.split(","))
                        .map(code -> Integer.parseInt(code.trim()))
                        .collect(Collectors.toList());
            }
        }
    }

    /**
     * 切面
     */
    @Pointcut("@annotation(com.gc.starter.log.annotation.Log)")
    public void logPointCut() {
        // 切点
    }

    /**
     * 环绕
     * @param point 切入点
     * @return 结果
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Exception exception = null;
        Object result = null;
        try {
            // 执行方法
            result = point.proceed();
        } catch (Exception e) {
            exception = e;
        }
        // 用时（毫秒）
        long time = System.currentTimeMillis() - beginTime;
        if (BooleanUtils.isTrue(this.logProperties.getConsole())) {
            // 执行耗时
            log.info("Time-Consuming : {} ms", time);
        }
        if (ObjectUtils.isNotEmpty(exception)) {
            // 待完善
            result = this.exceptionMessageHandler.message(exception, null);
        }
        this.saveLog(point, time, result);
        // 保存日志
        return result;
    }

    /**
     * 执行前
     * @param point 切点
     */
    @Before("logPointCut()")
    private void before(JoinPoint point) {
        if (BooleanUtils.isTrue(this.logProperties.getConsole())) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
            log.info("========================================== Start ==========================================");
            // 打印请求 url
            log.info("URL            : {}", request.getRequestURL().toString());
            // 打印 Http method
            log.info("HTTP Method    : {}", request.getMethod());
            // 打印调用 controller 的全路径以及执行方法
            log.info("Class Method   : {}.{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
            // 打印请求的 IP
            log.info("IP             : {}", IpUtils.getIpAddr());
        }
    }

    @After("logPointCut()")
    private void after() {
        if (BooleanUtils.isTrue(this.logProperties.getConsole())) {
            log.info("=========================================== End ===========================================");
        }
    }

    /**
     * 保存日志
     * @param point 切入点
     * @param time 用时
     * @param result 执行结果
     */
    @SuppressWarnings("rawtypes")
    private void saveLog(ProceedingJoinPoint point, long time, Object result) {
        try {
            final Signature signature = point.getSignature();
            if (signature instanceof MethodSignature) {
                final Method method = ((MethodSignature) signature).getMethod();
                final Log logAnnotation = method.getAnnotation(Log.class);
                int code = 200;
                boolean saveLog = true;
                // 错误信息
                String errorMessage = null;
                if (result instanceof Result) {
                    code = ((Result) result).getCode();
                    if (BooleanUtils.isFalse(((Result) result).isSuccess())) {
                        errorMessage = ((Result) result).getMessage();
                    }
                    List<Integer> saveCodeList = this.getCodeList();
                    // 如果设置了保存的编码，并且不包含保存的编码  则不保存日志
                    if (!saveCodeList.isEmpty() && !saveCodeList.contains(code)) {
                        saveLog = false;
                    }
                }
                if (saveLog) {
                    this.doSaveLog(point, result, signature, logAnnotation, time, code, errorMessage);
                }

            }
        } catch (Exception e) {
            log.error("保存日志发生错误", e);
        }
    }

    /**
     * 执行保存日志
     * @param point 切面
     * @param signature signature
     * @param logAnnotation 日志注解
     * @param time 用户
     * @param code 编码
     * @param errorMessage 错误信息
     */
    private void doSaveLog(ProceedingJoinPoint point, @Nullable Object result, Signature signature, Log logAnnotation, long time, int code, String errorMessage) {
        // 请求的方法名
        final String className = point.getTarget().getClass().getName();
        final String methodName = signature.getName();
        // 设置请求参数
        String parameter = JsonUtils.toJsonString(this.getParameter(point));
        final SysLogPO sysLog = SysLogPO.builder()
                .operation(logAnnotation.value())
                .useTime(time)
                .method(String.join(".", className, methodName))
                .ip(IpUtils.getIpAddr())
                .params(parameter)
                .requestPath(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getServletPath())
                .statusCode(code)
                .errorMessage(errorMessage)
                .type(logAnnotation.type().name())
                .ident(LogIdentConstant.AUTO.getValue())
                // todo：待处理
                .platform(null)
                .build();
        if (logAnnotation.saveResult() && Objects.nonNull(result)) {
            sysLog.setResult(JsonUtils.toJsonString(result));
        }
        this.logHandler.save(sysLog, point, logAnnotation, time, code, result, errorMessage);
    }

    /**
     * 获取请求参数
     * @param point point
     * @return parameter
     */
    private Map<String, Object> getParameter(ProceedingJoinPoint point) {
        final Map<String, Object> parameter = AopUtils.getParameterMap(point);
        final Map<String, Object> result = Maps.newHashMap();
        parameter.forEach((key, value) -> {
            if (Objects.isNull(value) || EXCLUDE_CLASS.stream().noneMatch(item -> item.isAssignableFrom(value.getClass()))) {
                result.put(key, value);
            }
        });
        return result;
    }

    /**
     * 获取编码集合
     * @return 允许的编码集合
     */
    private List<Integer> getCodeList() {
        return this.codeList;
    }
}
