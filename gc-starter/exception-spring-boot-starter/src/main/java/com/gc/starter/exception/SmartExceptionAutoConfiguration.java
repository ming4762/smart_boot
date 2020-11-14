package com.gc.starter.exception;

import com.gc.starter.exception.handler.AsyncNoticeHandler;
import com.gc.starter.exception.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 异常处理启动器自动配置类
 * @author shizhongming
 * 2020/11/15 12:00 上午
 */
@Configuration
public class SmartExceptionAutoConfiguration {

    /**
     * 创建异步通知执行器
     * @return 异步通知执行器
     */
    @Bean
    public AsyncNoticeHandler asyncNoticeHandler(ApplicationContext applicationContext) {
        return new AsyncNoticeHandler(applicationContext);
    }

    /**
     * 创建全局异常拦截器
     * @param asyncNoticeHandler 创建异步通知执行器
     * @return 创建全局异常拦截器
     */
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler(AsyncNoticeHandler asyncNoticeHandler) {
        return new GlobalExceptionHandler(asyncNoticeHandler);
    }

}
