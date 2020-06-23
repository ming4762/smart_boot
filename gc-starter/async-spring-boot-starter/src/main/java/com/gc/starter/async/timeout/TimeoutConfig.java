package com.gc.starter.async.timeout;

import com.gc.starter.async.timeout.aspect.TimeoutAspect;
import com.gc.starter.async.timeout.handler.DefaultTimeoutHandler;
import com.gc.starter.async.timeout.handler.TimeoutHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/5/28 2:55 下午
 */
@Configuration
public class TimeoutConfig {

    @Bean
    public TimeoutAspect timeoutAspect(ApplicationContext applicationContext) {
        return new TimeoutAspect(applicationContext);
    }

    /**
     * 构建默认timeout执行器
     * @return 默认timeout执行器
     */
    @Bean
    public TimeoutHandler timeoutHandler() {
        return new DefaultTimeoutHandler();
    }
}
