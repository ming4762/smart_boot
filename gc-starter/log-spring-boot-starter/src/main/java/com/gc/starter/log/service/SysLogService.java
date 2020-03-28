package com.gc.starter.log.service;

import com.gc.starter.crud.service.BaseService;
import com.gc.starter.log.model.SysLogPO;
import org.springframework.lang.NonNull;

/**
 * @author jackson
 * 2020/1/22 6:37 下午
 */
public interface SysLogService extends BaseService<SysLogPO> {


    /**
     * 保存异常日志
     * @param message 日志信息
     * @param e 错误信息
     * @return 是否保存成功
     */
    boolean saveException(String message, @NonNull Exception e);

    /**
     * 保存异常日志
     * @param e 异常信息
     * @return 是否保存成功
     */
    default boolean saveException(@NonNull Exception e) {
        return this.saveException(e.getMessage(), e);
    }
}
