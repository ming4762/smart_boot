package com.gc.starter.log.service.impl;

import com.gc.starter.crud.service.impl.BaseServiceImpl;
import com.gc.starter.log.mapper.SysLogMapper;
import com.gc.starter.log.model.SysLogPO;
import com.gc.starter.log.service.SysLogService;
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
}
