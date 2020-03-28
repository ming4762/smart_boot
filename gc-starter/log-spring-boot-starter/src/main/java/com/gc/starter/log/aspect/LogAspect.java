package com.gc.starter.log.aspect;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.message.Result;
import com.gc.common.base.utils.IpUtils;
import com.gc.starter.log.LogProperties;
import com.gc.starter.log.annotation.Log;
import com.gc.starter.log.constants.LogIdentConstant;
import com.gc.starter.log.model.SysLogPO;
import com.gc.starter.log.service.SysLogService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 日志切面
 * @author jackson
 * 2020/1/22 1:54 下午
 */
@Aspect
@Slf4j
public final class LogAspect {

    /**
     * 保存日志的编码集合
     */
    private static List<Integer> codeList;

    @Autowired
    private LogProperties logProperties;

    @Autowired
    private SysLogService logService;

    @PostConstruct
    public void init() {
        if (LogAspect.codeList == null) {
            String codes = this.logProperties.getCodes();
            if (StringUtils.isEmpty(codes)) {
                LogAspect.codeList = Lists.newArrayList();
            } else {
                LogAspect.codeList = Arrays.stream(codes.split(","))
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
     * @return
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 用时（毫秒）
        long time = System.currentTimeMillis() - beginTime;
        if (BooleanUtils.isTrue(this.logProperties.getConsole())) {
            // 执行耗时
            log.info("Time-Consuming : {} ms", time);
        }
        this.saveLog(point, time, result);
        // 保存日志
        return result;
    }

    /**
     * 执行前
     * @param point
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
                    if (BooleanUtils.isFalse(((Result) result).getOk())) {
                        errorMessage = ((Result) result).getMessage();
                    }
                    List<Integer> saveCodeList = this.getCodeList();
                    // 如果设置了保存的编码，并且不包含保存的编码  则不保存日志
                    if (!saveCodeList.isEmpty() && !saveCodeList.contains(code)) {
                        saveLog = false;
                    }
                }
                if (saveLog) {
                    this.doSaveLog(point, signature, logAnnotation, time, code, errorMessage);
                }

            }
        } catch (Exception e) {
            log.error("保存日志发生错误", e);
        }
    }

    /**
     * 执行保存日志
     * @param point
     * @param signature
     * @param logAnnotation
     * @param time
     * @param code
     * @param errorMessage
     */
    private void doSaveLog(ProceedingJoinPoint point, Signature signature, Log logAnnotation, long time, int code, String errorMessage) {
        // 请求的方法名
        final String className = point.getTarget().getClass().getName();
        final String methodName = signature.getName();
        // 设置请求参数
        Object[] args = point.getArgs();
        String parameter = "";
        if (args.length > 0) {
            parameter = JSON.toJSONString(args);
        }
        final SysLogPO sysLog = SysLogPO.builder()
                .logId(IdWorker.getId())
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
                // 待处理
                .platform(null)
                .build();
        this.logService.saveWithUser(sysLog, AuthUtils.getCurrentUserId());
    }

    /**
     * 获取编码集合
     * @return
     */
    private List<Integer> getCodeList() {
        return LogAspect.codeList;
    }
}
