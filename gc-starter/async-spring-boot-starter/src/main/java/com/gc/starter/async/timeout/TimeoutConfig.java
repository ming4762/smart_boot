package com.gc.starter.async.timeout;

import com.gc.starter.async.timeout.aspect.TimeoutAspect;
import com.gc.starter.async.timeout.handler.DefaultTimeoutHandler;
import com.gc.starter.async.timeout.handler.TimeoutHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/5/28 2:55 下午
 */
@Configuration
public class TimeoutConfig {

    @Bean
    public TimeoutAspect timeoutAspect(TimeoutHandler timeoutHandler) {
        return new TimeoutAspect(timeoutHandler);
    }

    /**
     * 构建超时执行器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public TimeoutHandler timeoutHandler() {
        return new DefaultTimeoutHandler();
    }
}
