package com.gc.database.integrate.controller;

import com.gc.common.base.message.Result;
import com.gc.database.integrate.model.DbTemplateDO;
import com.gc.database.integrate.service.DbTemplateService;
import com.gc.starter.crud.controller.BaseController;
import com.gc.starter.crud.query.PageQueryParameter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 模板管理controller
 * @author shizhongming
 * 2021/2/12 8:49 下午
 */
@RestController
@RequestMapping("db/template")
public class DbTemplateController extends BaseController<DbTemplateService, DbTemplateDO> {

    @Override
    @PostMapping("save")
    public Result<Boolean> save(@RequestBody DbTemplateDO model) {
        return super.save(model);
    }

    @Override
    @PostMapping("update")
    public Result<Boolean> update(@RequestBody DbTemplateDO model) {
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
    public Result<DbTemplateDO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
