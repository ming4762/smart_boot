package com.gc.database.integrate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.database.integrate.mapper.DatabaseConnectionMapper;
import com.gc.database.integrate.model.DatabaseConnectionDO;
import com.gc.database.integrate.service.DatabaseConnectionService;
import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shizhongming
 * 2020/10/30 9:43 下午
 */
@Service
public class DatabaseConnectionServiceImpl extends BaseServiceImpl<DatabaseConnectionMapper, DatabaseConnectionDO> implements DatabaseConnectionService {

    @Override
    public boolean save(DatabaseConnectionDO entity) {
        entity.setCreateTime(LocalDateTime.now());
        return super.save(entity);
    }

    @Override
    @NonNull
    public List<DatabaseConnectionDO> list(@NonNull QueryWrapper<DatabaseConnectionDO> queryWrapper, PageQueryParameter<String, Object> parameter, Boolean paging) {
        final List<DatabaseConnectionDO> list = super.list(queryWrapper, parameter, paging);
        list.forEach(item -> item.setPassword("xxxxx"));
        return list;
    }
}
