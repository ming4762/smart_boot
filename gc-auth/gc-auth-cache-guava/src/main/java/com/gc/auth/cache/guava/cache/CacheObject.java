package com.gc.auth.cache.guava.cache;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author shizhongming
 * 2020/9/11 9:46 下午
 */
@Getter
@Setter
@Builder
public class CacheObject {

    /**
     * 超时时间
     */
    private LocalDateTime operationTime;

    /**
     * 数据对象
     */
    private Object data;

    private Long timeout;
}
