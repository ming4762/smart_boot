package com.gc.database.integrate.service.impl;

import com.gc.database.integrate.mapper.DatabaseDicMapper;
import com.gc.database.integrate.model.DatabaseDicDO;
import com.gc.database.integrate.service.DatabaseDicService;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shizhongming
 * 2020/11/1 5:30 下午
 */
@Service
public class DatabaseDicServiceImpl extends BaseServiceImpl<DatabaseDicMapper, DatabaseDicDO> implements DatabaseDicService {
}
