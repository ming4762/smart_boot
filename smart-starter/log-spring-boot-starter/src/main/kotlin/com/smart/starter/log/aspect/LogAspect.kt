package com.smart.starter.log.aspect

import com.alibaba.fastjson.JSON
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.log.annotation.Log
import com.smart.common.message.Result
import com.smart.common.utils.IPUtils
import com.smart.starter.log.model.SysLogDO
import com.smart.starter.log.service.SysLogService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

/**
 * 日志切面
 * @author ming
 * TODO:异常捕获
 * 2019/6/28 下午4:23
 */
@Aspect
class LogAspect {

    @Autowired
    private lateinit var logService: SysLogService

    /**
     * 切面
     */
    @Pointcut("@annotation(com.smart.common.log.annotation.Log)")
    fun logPointCut() {
    }

    @Around("logPointCut()")
    @Throws(Throwable::class)
    fun around(point: ProceedingJoinPoint): Any? {
        val beginTime = System.currentTimeMillis()
        // 执行方法
        val result = point.proceed()
        // 执行时长(毫秒)
        val time = System.currentTimeMillis() - beginTime
        //异步保存日志
        this.saveLog(point, time, result)
        return result
    }

    @Throws(InterruptedException::class)
    private fun saveLog(joinPoint: ProceedingJoinPoint, time: Long, result: Any) {
        val sysLog = SysLogDO()
        // 设置用户
        sysLog.userId = AuthUtils.getCurrentUserId()
        // 获取注解日志描述
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val syslog = method.getAnnotation(Log::class.java)
        if (syslog != null) {
            // 注解上的描述
            sysLog.operation = syslog.value
        }
        // 设置用时
        sysLog.useTime = time.toInt()
        // 请求的方法名
        val className = joinPoint.target.javaClass.name
        val methodName = signature.name
        sysLog.method = "$className.$methodName()"
        // 请求参数
        val args = joinPoint.args
        if (args.isNotEmpty()) {
            sysLog.params = JSON.toJSONString(args[0])
        }
        // 获取IP地址
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        sysLog.ip = IPUtils.getIpAddr(request)
        sysLog.createTime = Date()
        sysLog.requestPath = request.servletPath
        // 设置code码
        if (result is Result<*>) {
            result.code?.let {
                sysLog.statusCode = it
            }
            if (!result.ok) {
                sysLog.errorMessage = result.message
            }
        }
        this.logService.save(sysLog)
    }
}