package com.gc.starter.crud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.base.message.Result;
import com.gc.starter.crud.constants.CrudConstants;
import com.gc.starter.crud.model.BaseModel;
import com.gc.starter.crud.model.PageData;
import com.gc.starter.crud.model.Sort;
import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.crud.service.BaseService;
import com.gc.starter.crud.utils.CrudUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础查询controller
 * @author shizhongming
 * 2020/1/12 6:08 下午
 */
@Slf4j
public abstract class BaseQueryController<K extends BaseService<T>, T extends BaseModel> {

    @Autowired
    protected K service;

    /**
     * list查询方法
     * @author jackson
     * @param parameter 查询参数
     * @return 查询结果集
     */
    protected Result<Object> list(@RequestBody PageQueryParameter<String, Object> parameter) {
        final Page<T> page = this.doPage(parameter);
        final QueryWrapper<T> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter, this.getModelType());
        final Object keyword = parameter.get(CrudConstants.KEYWORD.name());
        if (keyword instanceof String) {
            this.addKeyword(queryWrapper, (String) keyword);
        }
        final List<T> data = this.service.list(queryWrapper, parameter, page != null);
        if (page != null) {
            return Result.success(new PageData<T>(data, page.getTotal()));
        }
        return Result.success(data);
    }

    /**
     * 获取单一实体方法
     * @author jackson
     * @param model 包含主键信息的model
     * @return 结果
     */
    public Result<T> get(@RequestBody T model) {
        return Result.success(this.service.get(model));
    }

    /**
     * 执行分页
     * @param parameter
     * @return
     */
    @Nullable
    protected Page<T> doPage(@NonNull PageQueryParameter<String, Object> parameter) {
        Page<T> page = null;
        final Integer limit = parameter.getLimit();
        Integer offset = parameter.getOffset();
        if (limit != null) {
            if (ObjectUtils.isEmpty(offset)) {
                offset = 0;
            }
            // 解析排序字段
            final String orderMessage = this.analysisOrder(parameter);
            if (!StringUtils.isEmpty(orderMessage)) {
                PageHelper.orderBy(orderMessage);
            }
            page = PageHelper.offsetPage(offset, limit);
        }
        return page;
    }

    /**
     * 解析排序字段
     * @param parameter 前台参数
     * @return
     */
    @Nullable
    protected String analysisOrder(@NonNull PageQueryParameter<String, Object> parameter) {
        final String sortName = parameter.getSortName();
        if (!StringUtils.isEmpty(sortName)) {
            final String sortOrder = parameter.getSortOrder();
            final Class<? extends BaseModel> clazz = CrudUtils.getModelClassByType(this.getModelType());
            final List<Sort> sortList = CrudUtils.analysisOrder(sortName, sortOrder, clazz);
            if (sortList.isEmpty()) {
                return null;
            }
            return sortList
                    .stream()
                    .map(sort -> String.format("%s %s", sort.getDbName(), sort.getOrder()))
                    .collect(Collectors.joining(","));
        }
        return null;
    }


    /**
     * 获取实体类类型
     * @return 实体类类型
     */
    private Type getModelType() {
        return ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * 添加关键字查询
     * @param queryWrapper
     * @param keyword
     */
    private void addKeyword(@NonNull QueryWrapper<T> queryWrapper, @NonNull String keyword) {
        final Class<? extends BaseModel>  clazz = CrudUtils.getModelClassByType(getModelType());
        if (clazz != null) {
            final Field[] fieldList = clazz.getDeclaredFields();
            if (fieldList.length > 0) {
                queryWrapper.and(wrapper -> Arrays.asList(fieldList)
                        .forEach(field -> wrapper.or().like(CrudUtils.getDbField(field), keyword)));
            }
        }
    }
}
