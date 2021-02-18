package com.gc.database.integrate.controller;

import com.gc.common.base.message.Result;
import com.gc.database.integrate.model.DatabaseConnectionDO;
import com.gc.database.integrate.service.DatabaseConnectionService;
import com.gc.starter.crud.controller.BaseController;
import com.gc.starter.crud.query.PageQueryParameter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author shizhongming
 * 2020/10/30 9:44 下午
 */
@RestController
@RequestMapping("db/connection")
public class DatabaseConnectionController extends BaseController<DatabaseConnectionService, DatabaseConnectionDO> {

    @Override
    @PostMapping("save")
    public Result<Boolean> save(@RequestBody DatabaseConnectionDO model) {
        return super.save(model);
    }

    @Override
    @PostMapping("update")
    public Result<Boolean> update(@RequestBody DatabaseConnectionDO model) {
        return super.update(model);
    }

    @Override
    @PostMapping("batchDeleteById")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    @Override
    @PostMapping("list")
    public Result<Object> list(@RequestBody PageQueryParameter<String, Object> parameter) {
        return super.list(parameter);
    }

    @Override
    @PostMapping("getById")
    public Result<DatabaseConnectionDO> getById(@RequestBody Serializable id) {
        final Result<DatabaseConnectionDO> result = super.getById(id);
        if (Objects.nonNull(result.getData())) {
            result.getData().setPassword(null);
        }
        return result;
    }
}
