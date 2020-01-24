package com.gc.module.code.service.impl;

import com.gc.module.code.mapper.DatabaseConnectionMapper;
import com.gc.module.code.model.DatabaseConnectionPO;
import com.gc.module.code.service.DatabaseConnectionService;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jackson
 * 2020/1/21 10:11 下午
 */
@Service
public class DatabaseConnectionServiceImpl extends BaseServiceImpl<DatabaseConnectionMapper, DatabaseConnectionPO> implements DatabaseConnectionService {
}
