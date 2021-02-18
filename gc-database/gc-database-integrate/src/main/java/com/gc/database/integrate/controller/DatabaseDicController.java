package com.gc.database.integrate.controller;

import com.gc.common.base.message.Result;
import com.gc.database.integrate.model.DatabaseDicDO;
import com.gc.database.integrate.service.DatabaseDicService;
import com.gc.starter.crud.controller.BaseController;
import com.gc.starter.crud.query.PageQueryParameter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2020/11/1 5:30 下午
 */
@RestController
@RequestMapping("database/dic")
public class DatabaseDicController extends BaseController<DatabaseDicService, DatabaseDicDO> {

    @Override
    @PostMapping("save")
    public Result<Boolean> save(@RequestBody DatabaseDicDO model) {
        return super.save(model);
    }

    @Override
    @PostMapping("update")
    public Result<Boolean> update(@RequestBody DatabaseDicDO model) {
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
}
