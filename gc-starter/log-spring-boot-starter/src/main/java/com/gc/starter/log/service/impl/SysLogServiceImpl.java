package com.gc.starter.log.service.impl;

import com.gc.starter.crud.service.impl.BaseServiceImpl;
import com.gc.starter.log.mapper.SysLogMapper;
import com.gc.starter.log.model.SysLogPO;
import com.gc.starter.log.service.SysLogService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jackson
 * 2020/1/22 6:38 下午
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLogPO> implements SysLogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(SysLogPO entity) {
        return super.saveOrUpdate(entity);
    }

    /**
     * 保存异常日志
     * @param message 日志信息
     * @param e 错误信息
     * @return 是否保存成功
     */
    @Override
    public boolean saveException(String message, @Nullable Exception e) {

        return false;
    }
}
